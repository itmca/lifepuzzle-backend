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

	// Build database URL from individual components if DATABASE_URL not provided
	databaseURL := getEnv("DATABASE_URL", "")
	if databaseURL == "" {
		dbHost := getEnv("DB_HOST", "localhost")
		dbPort := getEnv("DB_PORT", "3306")
		dbUser := getEnv("DB_USER", "lifepuzzle")
		dbPassword := getEnv("DB_PASSWORD", "lifepuzzlepass")
		dbName := getEnv("DB_NAME", "lifepuzzle")
		databaseURL = dbUser + ":" + dbPassword + "@tcp(" + dbHost + ":" + dbPort + ")/" + dbName
	}

	return &Config{
		DatabaseURL:      databaseURL,
		RabbitMQURL:      getEnv("RABBITMQ_URL", "amqp://guest:guest@localhost:5672/"),
		AWSRegion:        getEnv("AWS_REGION", "us-east-1"),
		S3Bucket:         getEnvWithFallback("S3_BUCKET", "AWS_S3_BUCKET", "lifepuzzle-images"),
		AWSAccessKeyID:   getEnvWithFallback("AWS_ACCESS_KEY_ID", "AWS_ACCESS_KEY", ""),
		AWSSecretKey:     getEnvWithFallback("AWS_SECRET_ACCESS_KEY", "AWS_SECRET_KEY", ""),
		QueueName:        getEnv("QUEUE_NAME", "image-resize-queue"),
	}, nil
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

func getEnvWithFallback(primaryKey, fallbackKey, defaultValue string) string {
	if value := os.Getenv(primaryKey); value != "" {
		return value
	}
	if value := os.Getenv(fallbackKey); value != "" {
		return value
	}
	return defaultValue
}