package main

import (
	"log"
	"os"
	"os/signal"
	"syscall"

	"lifepuzzle-monorepo/services/image-resizer/config"
	"lifepuzzle-monorepo/services/image-resizer/database"
	"lifepuzzle-monorepo/services/image-resizer/messaging"
	"lifepuzzle-monorepo/services/image-resizer/resizer"
	"lifepuzzle-monorepo/services/image-resizer/storage"
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

	originalImage, err := s3Client.DownloadImage(photo.Url)
	if err != nil {
		return err
	}

	newSizes := append([]int{}, photo.ResizedSizes...)

	for _, size := range missingSizes {
		resizedImage := imageResizer.ResizeImage(originalImage, size)
		resizedPath := imageResizer.GenerateResizedPath(photo.Url, size)

		if err := s3Client.UploadImage(resizedPath, resizedImage); err != nil {
			return err
		}

		newSizes = append(newSizes, size)
		log.Printf("Created resized image for photo ID %d at size %d", msg.ID, size)
	}

	if err := db.UpdateResizedSizes(msg.ID, newSizes); err != nil {
		return err
	}

	log.Printf("Successfully processed photo ID: %d", msg.ID)
	return nil
}
