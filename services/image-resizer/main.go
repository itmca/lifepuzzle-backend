package main

import (
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"path/filepath"
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

	consumer, err := messaging.NewRabbitMQConsumer(cfg.RabbitMQURL, cfg.QueueName)
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
				log.Printf("Failed to process message %d: %v", msg.ID, err)
				if nackErr := msg.Nack(); nackErr != nil {
					log.Printf("Failed to nack message %d: %v", msg.ID, nackErr)
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

	for _, size := range missingSizes {
		resizedImage := imageResizer.ResizeImage(originalImage, size)
		resizedPath := imageResizer.GenerateResizedPath(photo.Url, size)

		// Convert to WebP and upload as bytes
		webpBytes, err := imageResizer.ConvertToWebP(resizedImage)
		if err != nil {
			return err
		}

		if err := s3Client.UploadImageBytes(resizedPath, webpBytes); err != nil {
			return err
		}

		newSizes = append(newSizes, size)
		log.Printf("Created resized WebP image for photo ID %d at size %d", msg.ID, size)
	}

	// Also convert the original image to WebP if it's not already WebP and larger than 1280px
	originalBounds := originalImage.Bounds()
	originalWidth := originalBounds.Dx()
	originalHeight := originalBounds.Dy()

	if (originalWidth > 1280 || originalHeight > 1280) && !strings.HasSuffix(strings.ToLower(photo.Url), ".webp") {
		log.Printf("Converting original image to WebP for photo ID: %d", msg.ID)

		processedBytes, err := imageResizer.ProcessImage(originalImageBytes)
		if err != nil {
			return err
		}

		// Generate WebP path for the original
		webpPath := strings.TrimSuffix(photo.Url, filepath.Ext(photo.Url)) + ".webp"

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
