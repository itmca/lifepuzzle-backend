package config

import (
	"os"

	"github.com/joho/godotenv"
)

type Config struct {
	DatabaseURL      string
	RabbitMQURL      string
	AWSRegion        string
	S3Bucket         string
	AWSAccessKeyID   string
	AWSSecretKey     string
	QueueName        string
}

func Load() (*Config, error) {
	godotenv.Load()

	return &Config{
		DatabaseURL:      getEnv("DATABASE_URL", "user:password@tcp(localhost:3306)/lifepuzzle"),
		RabbitMQURL:      getEnv("RABBITMQ_URL", "amqp://guest:guest@localhost:5672/"),
		AWSRegion:        getEnv("AWS_REGION", "us-east-1"),
		S3Bucket:         getEnv("S3_BUCKET", "lifepuzzle-images"),
		AWSAccessKeyID:   getEnv("AWS_ACCESS_KEY_ID", ""),
		AWSSecretKey:     getEnv("AWS_SECRET_ACCESS_KEY", ""),
		QueueName:        getEnv("QUEUE_NAME", "image-resize-queue"),
	}, nil
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}