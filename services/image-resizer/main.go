package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"path/filepath"
	"regexp"
	"strconv"
	"strings"
	"syscall"
	"time"

	"lifepuzzle-backend/services/image-resizer/config"
	"lifepuzzle-backend/services/image-resizer/database"
	"lifepuzzle-backend/services/image-resizer/messaging"
	"lifepuzzle-backend/services/image-resizer/resizer"
	"lifepuzzle-backend/services/image-resizer/storage"
)

func main() {
	cfg, err := config.Load()
	if err != nil {
		log.Fatalf("Failed to load configuration: %v", err)
	}

	db, err := database.NewDatabase(cfg.DatabaseURL)
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	s3Client, err := storage.NewS3Client(cfg.AWSRegion, cfg.AWSAccessKeyID, cfg.AWSSecretKey, cfg.S3Bucket)
	if err != nil {
		log.Fatalf("Failed to create S3 client: %v", err)
	}

	consumer, err := messaging.NewRabbitMQConsumer(cfg.RabbitMQURL, cfg.QueueName, cfg.ExchangeName, cfg.RoutingKey)
	if err != nil {
		log.Fatalf("Failed to create RabbitMQ consumer: %v", err)
	}
	defer consumer.Close()

	imageResizer := resizer.NewImageResizer()

	messages, err := consumer.Consume()
	if err != nil {
		log.Fatalf("Failed to start consuming messages: %v", err)
	}

	// Start HTTP health check server
	go startHealthServer()

	// Start Admin server
	go startAdminServer(db, cfg)

	log.Println("Image resizer service started. Waiting for messages...")

	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)

	for {
		select {
		case msg := <-messages:
			if err := processMessage(msg, db, s3Client, imageResizer); err != nil {
				log.Printf("Failed to process message %d (attempt %d): %v", msg.ID, msg.RetryCount+1, err)
				
				// Implement retry logic with exponential backoff
				const maxRetries = 3
				if msg.RetryCount >= maxRetries {
					log.Printf("Message %d exceeded max retries (%d), sending to DLQ", msg.ID, maxRetries)
					if dlqErr := msg.SendToDLQ(); dlqErr != nil {
						log.Printf("Failed to send message %d to DLQ: %v", msg.ID, dlqErr)
						// Fallback: nack without requeue to avoid infinite loops
						if nackErr := msg.NackNoRequeue(); nackErr != nil {
							log.Printf("Failed to nack message %d without requeue: %v", msg.ID, nackErr)
						}
					} else {
						log.Printf("Message %d successfully sent to DLQ", msg.ID)
					}
				} else {
					log.Printf("Requeuing message %d for retry (attempt %d/%d)", msg.ID, msg.RetryCount+1, maxRetries)
					if nackErr := msg.Nack(); nackErr != nil {
						log.Printf("Failed to nack message %d: %v", msg.ID, nackErr)
					}
				}
			} else {
				if ackErr := msg.Ack(); ackErr != nil {
					log.Printf("Failed to ack message %d: %v", msg.ID, ackErr)
				}
			}
		case <-quit:
			log.Println("Shutting down service...")
			return
		}
	}
}

func processMessage(msg messaging.Message, db *database.Database, s3Client *storage.S3Client, imageResizer *resizer.ImageResizer) error {
	log.Printf("Processing message for photo ID: %d", msg.ID)

	photo, err := db.GetStoryPhoto(msg.ID)
	if err != nil {
		return err
	}

	missingSizes := imageResizer.GetMissingSizes(photo.ResizedSizes)
	if len(missingSizes) == 0 {
		log.Printf("No missing sizes for photo ID: %d", msg.ID)
		return nil
	}

	log.Printf("Missing sizes for photo ID %d: %v", msg.ID, missingSizes)

	// Organize photo into proper directory structure if needed
	if err := organizePhotoStructure(photo, s3Client, db); err != nil {
		return err
	}

	// Download original image as bytes to support WebP conversion
	originalImageBytes, err := s3Client.DownloadImageBytes(photo.Url)
	if err != nil {
		return err
	}

	// Decode the original image using our custom decoder that supports WebP
	originalImage, err := imageResizer.DecodeImage(originalImageBytes)
	if err != nil {
		return err
	}

	newSizes := append([]int{}, photo.ResizedSizes...)

	// Use hero ID and photo ID from database (no need to parse URL)
	heroId := photo.HeroID  // Assuming this field exists in StoryPhoto
	photoId := photo.ID

	// Get original image dimensions
	originalBounds := originalImage.Bounds()
	originalWidth := originalBounds.Dx()
	originalHeight := originalBounds.Dy()
	isOriginalWebP := strings.HasSuffix(strings.ToLower(photo.Url), ".webp")

	for _, size := range missingSizes {
		filename := filepath.Base(photo.Url)
		webpFilename := strings.TrimSuffix(filename, filepath.Ext(filename)) + ".webp"
		resizedPath := fmt.Sprintf("hero/%d/image/%d/%d/%s", heroId, photoId, size, webpFilename)

		// Special case for 1280: if original is WebP and smaller than 1280, use original
		if size == 1280 && isOriginalWebP && originalWidth <= 1280 && originalHeight <= 1280 {
			log.Printf("Using original WebP image for 1280 size (photo ID %d): original size %dx%d", msg.ID, originalWidth, originalHeight)
			
			if err := s3Client.UploadImageBytes(resizedPath, originalImageBytes); err != nil {
				return err
			}
		} else {
			// Normal resizing process
			resizedImage := imageResizer.ResizeImage(originalImage, size)
			
			// Convert to WebP and upload as bytes
			webpBytes, err := imageResizer.ConvertToWebP(resizedImage)
			if err != nil {
				return err
			}

			if err := s3Client.UploadImageBytes(resizedPath, webpBytes); err != nil {
				return err
			}
		}

		newSizes = append(newSizes, size)
		log.Printf("Created resized WebP image for photo ID %d at size %d: %s", msg.ID, size, resizedPath)
	}

	if err := db.UpdateResizedSizes(msg.ID, newSizes); err != nil {
		return err
	}

	log.Printf("Successfully processed photo ID: %d", msg.ID)
	return nil
}

func startHealthServer() {
	http.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		fmt.Fprintf(w, "OK")
	})

	port := os.Getenv("PORT")
	if port == "" {
		port = "9000"
	}

	log.Printf("Health check server starting on port %s", port)
	if err := http.ListenAndServe(":"+port, nil); err != nil {
		log.Printf("Health server error: %v", err)
	}
}

func startAdminServer(db *database.Database, cfg *config.Config) {
	// Create RabbitMQ producer for sending messages
	producer, err := messaging.NewRabbitMQProducer(cfg.RabbitMQURL, cfg.ExchangeName, cfg.RoutingKey)
	if err != nil {
		log.Printf("Failed to create RabbitMQ producer for admin server: %v", err)
		return
	}
	defer producer.Close()

	adminMux := http.NewServeMux()

	// Admin status endpoint
	adminMux.HandleFunc("/admin/photo-reprocessing/status", func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodGet {
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
			return
		}

		totalImages, err := db.CountGalleries()
		if err != nil {
			log.Printf("Failed to count galleries: %v", err)
			http.Error(w, "Failed to count galleries", http.StatusInternalServerError)
			return
		}

		galleries, err := db.GetAllGalleries()
		if err != nil {
			log.Printf("Failed to get galleries: %v", err)
			http.Error(w, "Failed to get galleries", http.StatusInternalServerError)
			return
		}

		needsReprocessing := 0
		for _, gallery := range galleries {
			if gallery.IsImage() && len(gallery.ResizedSizes) < 3 {
				needsReprocessing++
			}
		}

		status := map[string]interface{}{
			"timestamp":         time.Now(),
			"totalImages":       totalImages,
			"needsReprocessing": needsReprocessing,
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(status)
	})

	// Reprocess missing sizes endpoint
	adminMux.HandleFunc("/admin/photo-reprocessing/reprocess-missing-sizes", func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodPost {
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
			return
		}

		// Parse query parameters
		batchSize := 50
		delayMs := int64(1000)
		
		if bs := r.URL.Query().Get("batchSize"); bs != "" {
			if parsed, err := strconv.Atoi(bs); err == nil {
				batchSize = parsed
			}
		}
		
		if dm := r.URL.Query().Get("delayMs"); dm != "" {
			if parsed, err := strconv.ParseInt(dm, 10, 64); err == nil {
				delayMs = parsed
			}
		}

		log.Printf("Starting photo reprocessing for missing sizes - batchSize: %d, delayMs: %d", batchSize, delayMs)

		galleries, err := db.GetAllGalleries()
		if err != nil {
			log.Printf("Failed to get galleries: %v", err)
			http.Error(w, "Failed to get galleries", http.StatusInternalServerError)
			return
		}

		// Filter galleries that need reprocessing
		var needsReprocessing []*database.Gallery
		for _, gallery := range galleries {
			if gallery.IsImage() && len(gallery.ResizedSizes) < 3 {
				needsReprocessing = append(needsReprocessing, gallery)
			}
		}

		log.Printf("Found %d images that need reprocessing", len(needsReprocessing))

		processedCount := 0
		successCount := 0
		errorCount := 0

		// Process in batches
		for i := 0; i < len(needsReprocessing); i += batchSize {
			endIndex := i + batchSize
			if endIndex > len(needsReprocessing) {
				endIndex = len(needsReprocessing)
			}

			log.Printf("Processing batch %d-%d of %d", i+1, endIndex, len(needsReprocessing))

			for j := i; j < endIndex; j++ {
				gallery := needsReprocessing[j]
				if err := producer.SendMessage(gallery.ID); err != nil {
					errorCount++
					log.Printf("Error sending reprocessing message for photo ID: %d, error: %v", gallery.ID, err)
				} else {
					successCount++
				}
				processedCount++
			}

			// Batch delay
			if endIndex < len(needsReprocessing) {
				time.Sleep(time.Duration(delayMs) * time.Millisecond)
			}
		}

		result := map[string]interface{}{
			"timestamp":   time.Now(),
			"totalFound":  len(needsReprocessing),
			"processed":   processedCount,
			"successful":  successCount,
			"errors":      errorCount,
			"batchSize":   batchSize,
			"delayMs":     delayMs,
		}

		log.Printf("Photo reprocessing completed - processed: %d, successful: %d, errors: %d", 
			processedCount, successCount, errorCount)

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(result)
	})

	// Reprocess all photos endpoint
	adminMux.HandleFunc("/admin/photo-reprocessing/reprocess-all", func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodPost {
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
			return
		}

		// Parse query parameters
		batchSize := 50
		delayMs := int64(1000)
		var startID, endID *int
		
		if bs := r.URL.Query().Get("batchSize"); bs != "" {
			if parsed, err := strconv.Atoi(bs); err == nil {
				batchSize = parsed
			}
		}
		
		if dm := r.URL.Query().Get("delayMs"); dm != "" {
			if parsed, err := strconv.ParseInt(dm, 10, 64); err == nil {
				delayMs = parsed
			}
		}
		
		if si := r.URL.Query().Get("startId"); si != "" {
			if parsed, err := strconv.Atoi(si); err == nil {
				startID = &parsed
			}
		}
		
		if ei := r.URL.Query().Get("endId"); ei != "" {
			if parsed, err := strconv.Atoi(ei); err == nil {
				endID = &parsed
			}
		}

		log.Printf("Starting full photo reprocessing - batchSize: %d, delayMs: %d, startId: %v, endId: %v", 
			batchSize, delayMs, startID, endID)

		galleries, err := db.GetAllGalleries()
		if err != nil {
			log.Printf("Failed to get galleries: %v", err)
			http.Error(w, "Failed to get galleries", http.StatusInternalServerError)
			return
		}

		// Filter galleries by ID range and image type
		var allImages []*database.Gallery
		for _, gallery := range galleries {
			if !gallery.IsImage() {
				continue
			}
			if startID != nil && gallery.ID < *startID {
				continue
			}
			if endID != nil && gallery.ID > *endID {
				continue
			}
			allImages = append(allImages, gallery)
		}

		log.Printf("Found %d images to reprocess", len(allImages))

		processedCount := 0
		successCount := 0
		errorCount := 0

		// Process in batches
		for i := 0; i < len(allImages); i += batchSize {
			endIndex := i + batchSize
			if endIndex > len(allImages) {
				endIndex = len(allImages)
			}

			log.Printf("Processing batch %d-%d of %d", i+1, endIndex, len(allImages))

			for j := i; j < endIndex; j++ {
				gallery := allImages[j]
				if err := producer.SendMessage(gallery.ID); err != nil {
					errorCount++
					log.Printf("Error sending reprocessing message for photo ID: %d, error: %v", gallery.ID, err)
				} else {
					successCount++
				}
				processedCount++
			}

			// Batch delay
			if endIndex < len(allImages) {
				time.Sleep(time.Duration(delayMs) * time.Millisecond)
			}
		}

		result := map[string]interface{}{
			"timestamp":  time.Now(),
			"totalFound": len(allImages),
			"processed":  processedCount,
			"successful": successCount,
			"errors":     errorCount,
			"batchSize":  batchSize,
			"delayMs":    delayMs,
			"startId":    startID,
			"endId":      endID,
		}

		log.Printf("Full photo reprocessing completed - processed: %d, successful: %d, errors: %d", 
			processedCount, successCount, errorCount)

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(result)
	})

	adminPort := os.Getenv("ADMIN_PORT")
	if adminPort == "" {
		adminPort = "9001"
	}

	log.Printf("Admin server starting on port %s", adminPort)
	if err := http.ListenAndServe(":"+adminPort, adminMux); err != nil {
		log.Printf("Admin server error: %v", err)
	}
}

// organizePhotoStructure moves photo to organized directory structure if not already organized
func organizePhotoStructure(photo *database.StoryPhoto, s3Client *storage.S3Client, db *database.Database) error {
	// Check if photo is already in organized structure (hero/{heroId}/image/{photoId}/original/)
	if isAlreadyOrganized(photo.Url, int64(photo.ID)) {
		log.Printf("Photo %d is already organized: %s", photo.ID, photo.Url)
		return nil
	}

	// Use hero ID from database instead of parsing URL
	heroId := photo.HeroID
	
	// Generate new organized path: hero/{heroId}/image/{photoId}/original/{filename}
	filename := filepath.Base(photo.Url)
	newPath := fmt.Sprintf("hero/%d/image/%d/original/%s", heroId, photo.ID, filename)

	log.Printf("Moving photo %d from %s to %s", photo.ID, photo.Url, newPath)

	// Download the image from old location
	imageBytes, err := s3Client.DownloadImageBytes(photo.Url)
	if err != nil {
		return fmt.Errorf("failed to download image from old location %s: %w", photo.Url, err)
	}

	// Upload to new location
	if err := s3Client.UploadImageBytes(newPath, imageBytes); err != nil {
		return fmt.Errorf("failed to upload image to new location %s: %w", newPath, err)
	}

	// Update photo URL in database
	if err := db.UpdateStoryPhotoUrl(photo.ID, newPath); err != nil {
		return fmt.Errorf("failed to update photo URL in database: %w", err)
	}
	
	// Update local photo object for consistency
	photo.Url = newPath

	// Delete old location (optional, but recommended to avoid duplication)
	// Note: We're not implementing delete here to be safe, but could be added later
	log.Printf("Successfully organized photo %d to new location: %s", photo.ID, newPath)

	return nil
}

// isAlreadyOrganized checks if photo URL follows the organized structure
func isAlreadyOrganized(url string, photoId int64) bool {
	// Pattern: hero/{heroId}/image/{photoId}/original/{filename} or hero/{heroId}/image/{photoId}/{size}/{filename}
	pattern := fmt.Sprintf(`hero/\d+/image/%d/(original|\d+)/[^/]+$`, photoId)
	matched, _ := regexp.MatchString(pattern, url)
	return matched
}

