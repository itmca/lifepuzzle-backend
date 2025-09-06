package main

import (
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
	organizedUrl, err := organizePhotoStructure(photo, s3Client)
	if err != nil {
		return err
	}

	// Download original image as bytes to support WebP conversion
	originalImageBytes, err := s3Client.DownloadImageBytes(organizedUrl)
	if err != nil {
		return err
	}

	// Decode the original image using our custom decoder that supports WebP
	originalImage, err := imageResizer.DecodeImage(originalImageBytes)
	if err != nil {
		return err
	}

	newSizes := append([]int{}, photo.ResizedSizes...)

	// Extract hero ID and photo ID from organized URL
	heroId, photoId, err := extractIdsFromUrl(organizedUrl)
	if err != nil {
		return err
	}

	for _, size := range missingSizes {
		resizedImage := imageResizer.ResizeImage(originalImage, size)
		
		// Generate new structured path: hero/{heroId}/image/{photoId}/{size}/filename.webp
		filename := filepath.Base(organizedUrl)
		webpFilename := strings.TrimSuffix(filename, filepath.Ext(filename)) + ".webp"
		resizedPath := fmt.Sprintf("hero/%d/image/%d/%d/%s", heroId, photoId, size, webpFilename)

		// Convert to WebP and upload as bytes
		webpBytes, err := imageResizer.ConvertToWebP(resizedImage)
		if err != nil {
			return err
		}

		if err := s3Client.UploadImageBytes(resizedPath, webpBytes); err != nil {
			return err
		}

		newSizes = append(newSizes, size)
		log.Printf("Created resized WebP image for photo ID %d at size %d: %s", msg.ID, size, resizedPath)
	}

	// Also convert the original image to WebP if it's not already WebP and larger than 1280px
	originalBounds := originalImage.Bounds()
	originalWidth := originalBounds.Dx()
	originalHeight := originalBounds.Dy()

	if (originalWidth > 1280 || originalHeight > 1280) && !strings.HasSuffix(strings.ToLower(organizedUrl), ".webp") {
		log.Printf("Converting original image to WebP for photo ID: %d", msg.ID)

		processedBytes, err := imageResizer.ProcessImage(originalImageBytes)
		if err != nil {
			return err
		}

		// Generate WebP path for the original with new structure
		filename := filepath.Base(organizedUrl)
		webpFilename := strings.TrimSuffix(filename, filepath.Ext(filename)) + ".webp"
		webpPath := fmt.Sprintf("hero/%d/image/%d/original/%s", heroId, photoId, webpFilename)

		if err := s3Client.UploadImageBytes(webpPath, processedBytes); err != nil {
			return err
		}

		log.Printf("Converted original image to WebP: %s", webpPath)
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

// organizePhotoStructure moves photo to organized directory structure if not already organized
func organizePhotoStructure(photo *database.StoryPhoto, s3Client *storage.S3Client) (string, error) {
	// Check if photo is already in organized structure (hero/{heroId}/image/{photoId}/original/)
	if isAlreadyOrganized(photo.Url, photo.ID) {
		log.Printf("Photo %d is already organized: %s", photo.ID, photo.Url)
		return photo.Url, nil
	}

	// Extract hero ID from current URL pattern: hero/{heroId}/image/{filename}
	heroId, err := extractHeroIdFromCurrentUrl(photo.Url)
	if err != nil {
		return "", fmt.Errorf("failed to extract hero ID from URL %s: %w", photo.Url, err)
	}

	// Generate new organized path: hero/{heroId}/image/{photoId}/original/{filename}
	filename := filepath.Base(photo.Url)
	newPath := fmt.Sprintf("hero/%d/image/%d/original/%s", heroId, photo.ID, filename)

	log.Printf("Moving photo %d from %s to %s", photo.ID, photo.Url, newPath)

	// Download the image from old location
	imageBytes, err := s3Client.DownloadImageBytes(photo.Url)
	if err != nil {
		return "", fmt.Errorf("failed to download image from old location %s: %w", photo.Url, err)
	}

	// Upload to new location
	if err := s3Client.UploadImageBytes(newPath, imageBytes); err != nil {
		return "", fmt.Errorf("failed to upload image to new location %s: %w", newPath, err)
	}

	// Delete old location (optional, but recommended to avoid duplication)
	// Note: We're not implementing delete here to be safe, but could be added later
	log.Printf("Successfully organized photo %d to new location: %s", photo.ID, newPath)

	return newPath, nil
}

// isAlreadyOrganized checks if photo URL follows the organized structure
func isAlreadyOrganized(url string, photoId int64) bool {
	// Pattern: hero/{heroId}/image/{photoId}/original/{filename} or hero/{heroId}/image/{photoId}/{size}/{filename}
	pattern := fmt.Sprintf(`hero/\d+/image/%d/(original|\d+)/[^/]+$`, photoId)
	matched, _ := regexp.MatchString(pattern, url)
	return matched
}

// extractHeroIdFromCurrentUrl extracts hero ID from current URL format: hero/{heroId}/image/{filename}
func extractHeroIdFromCurrentUrl(url string) (int64, error) {
	// Pattern: hero/{heroId}/image/{filename}
	re := regexp.MustCompile(`hero/(\d+)/image/[^/]+$`)
	matches := re.FindStringSubmatch(url)
	if len(matches) != 2 {
		return 0, fmt.Errorf("URL does not match expected pattern: %s", url)
	}
	
	heroId, err := strconv.ParseInt(matches[1], 10, 64)
	if err != nil {
		return 0, fmt.Errorf("failed to parse hero ID: %w", err)
	}
	
	return heroId, nil
}

// extractIdsFromUrl extracts hero ID and photo ID from organized URL
func extractIdsFromUrl(url string) (int64, int64, error) {
	// Pattern: hero/{heroId}/image/{photoId}/{size_or_original}/{filename}
	re := regexp.MustCompile(`hero/(\d+)/image/(\d+)/(original|\d+)/[^/]+$`)
	matches := re.FindStringSubmatch(url)
	if len(matches) != 4 {
		return 0, 0, fmt.Errorf("URL does not match organized pattern: %s", url)
	}
	
	heroId, err := strconv.ParseInt(matches[1], 10, 64)
	if err != nil {
		return 0, 0, fmt.Errorf("failed to parse hero ID: %w", err)
	}
	
	photoId, err := strconv.ParseInt(matches[2], 10, 64)
	if err != nil {
		return 0, 0, fmt.Errorf("failed to parse photo ID: %w", err)
	}
	
	return heroId, photoId, nil
}
