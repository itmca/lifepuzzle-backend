package main

import (
	"database/sql"
	"encoding/json"
	"flag"
	"fmt"
	"log"
	"os"
	"time"

	_ "github.com/go-sql-driver/mysql"
	amqp "github.com/rabbitmq/amqp091-go"
)

type Config struct {
	DatabaseURL   string
	RabbitMQURL   string
	ExchangeName  string
	RoutingKey    string
	QueueName     string
	BatchSize     int
	DelayMs       int
	DryRun        bool
	MissingOnly   bool
	StartID       *int
	EndID         *int
}

type Photo struct {
	ID           int    `json:"id"`
	HeroID       int    `json:"hero_id"`
	URL          string `json:"url"`
	ResizedSizes []int  `json:"resized_sizes"`
	Type         string `json:"type"`
}

type QueueMessage struct {
	ID int `json:"id"`
}

func main() {
	config := parseFlags()
	
	if err := validateConfig(config); err != nil {
		log.Fatalf("Configuration error: %v", err)
	}
	
	log.Printf("Starting photo reprocessing script with config: %+v", config)
	
	// Connect to database
	db, err := connectDatabase(config.DatabaseURL)
	if err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()
	
	// Connect to RabbitMQ (skip if dry run)
	var publisher *RabbitMQPublisher
	if !config.DryRun {
		publisher, err = NewRabbitMQPublisher(config.RabbitMQURL, config.ExchangeName, config.RoutingKey, config.QueueName)
		if err != nil {
			log.Fatalf("Failed to connect to RabbitMQ: %v", err)
		}
		defer publisher.Close()
	}
	
	// Get photos to process
	photos, err := getPhotosToProcess(db, config)
	if err != nil {
		log.Fatalf("Failed to get photos: %v", err)
	}
	
	log.Printf("Found %d photos to process", len(photos))
	
	if len(photos) == 0 {
		log.Println("No photos to process. Exiting.")
		return
	}
	
	// Process photos in batches
	err = processPhotos(publisher, photos, config)
	if err != nil {
		log.Fatalf("Failed to process photos: %v", err)
	}
	
	log.Println("Photo reprocessing completed successfully!")
}

func parseFlags() Config {
	config := Config{}
	
	flag.StringVar(&config.DatabaseURL, "db-url", os.Getenv("DATABASE_URL"), "Database connection URL")
	flag.StringVar(&config.RabbitMQURL, "rabbitmq-url", os.Getenv("RABBITMQ_URL"), "RabbitMQ connection URL")
	flag.StringVar(&config.ExchangeName, "exchange", "image-processing", "RabbitMQ exchange name")
	flag.StringVar(&config.RoutingKey, "routing-key", "image.resize", "RabbitMQ routing key")
	flag.StringVar(&config.QueueName, "queue", "image-resize-queue", "RabbitMQ queue name")
	flag.IntVar(&config.BatchSize, "batch-size", 50, "Number of photos to process in each batch")
	flag.IntVar(&config.DelayMs, "delay-ms", 1000, "Delay between batches in milliseconds")
	flag.BoolVar(&config.DryRun, "dry-run", false, "Print what would be done without actually sending messages")
	flag.BoolVar(&config.MissingOnly, "missing-only", true, "Only process photos with missing resized sizes")
	
	var startID, endID int
	flag.IntVar(&startID, "start-id", 0, "Start processing from this photo ID (0 = no limit)")
	flag.IntVar(&endID, "end-id", 0, "Stop processing at this photo ID (0 = no limit)")
	
	flag.Parse()
	
	if startID > 0 {
		config.StartID = &startID
	}
	if endID > 0 {
		config.EndID = &endID
	}
	
	return config
}

func validateConfig(config Config) error {
	if config.DatabaseURL == "" {
		return fmt.Errorf("database URL is required")
	}
	if !config.DryRun && config.RabbitMQURL == "" {
		return fmt.Errorf("RabbitMQ URL is required (unless using --dry-run)")
	}
	if config.BatchSize <= 0 {
		return fmt.Errorf("batch size must be positive")
	}
	return nil
}

func connectDatabase(databaseURL string) (*sql.DB, error) {
	db, err := sql.Open("mysql", databaseURL)
	if err != nil {
		return nil, err
	}
	
	if err := db.Ping(); err != nil {
		return nil, err
	}
	
	log.Println("Successfully connected to database")
	return db, nil
}

func getPhotosToProcess(db *sql.DB, config Config) ([]Photo, error) {
	query := `
		SELECT id, hero_id, url, resized_sizes, type 
		FROM story_photo 
		WHERE type = 'IMAGE'
	`
	args := []interface{}{}
	
	// Add ID range filters if specified
	if config.StartID != nil {
		query += " AND id >= ?"
		args = append(args, *config.StartID)
	}
	if config.EndID != nil {
		query += " AND id <= ?"
		args = append(args, *config.EndID)
	}
	
	query += " ORDER BY id"
	
	log.Printf("Executing query: %s with args: %v", query, args)
	
	rows, err := db.Query(query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	
	var allPhotos []Photo
	for rows.Next() {
		var photo Photo
		var resizedSizesJSON []byte
		var photoType sql.NullString
		
		err := rows.Scan(&photo.ID, &photo.HeroID, &photo.URL, &resizedSizesJSON, &photoType)
		if err != nil {
			return nil, err
		}
		
		if photoType.Valid {
			photo.Type = photoType.String
		}
		
		// Parse resized sizes JSON
		if len(resizedSizesJSON) > 0 {
			if err := json.Unmarshal(resizedSizesJSON, &photo.ResizedSizes); err != nil {
				log.Printf("Warning: failed to unmarshal resized sizes for photo ID %d: %v", photo.ID, err)
				photo.ResizedSizes = []int{}
			}
		}
		
		allPhotos = append(allPhotos, photo)
	}
	
	if err := rows.Err(); err != nil {
		return nil, err
	}
	
	log.Printf("Loaded %d photos from database", len(allPhotos))
	
	// Filter for missing sizes if requested
	if config.MissingOnly {
		var filtered []Photo
		for _, photo := range allPhotos {
			if len(photo.ResizedSizes) < 3 { // Assuming 3 target sizes: 1280, 640, 240
				filtered = append(filtered, photo)
			}
		}
		log.Printf("Filtered to %d photos with missing sizes", len(filtered))
		return filtered, nil
	}
	
	return allPhotos, nil
}

func processPhotos(publisher *RabbitMQPublisher, photos []Photo, config Config) error {
	totalPhotos := len(photos)
	successCount := 0
	errorCount := 0
	
	// Process in batches
	for i := 0; i < totalPhotos; i += config.BatchSize {
		endIndex := i + config.BatchSize
		if endIndex > totalPhotos {
			endIndex = totalPhotos
		}
		
		batch := photos[i:endIndex]
		log.Printf("Processing batch %d-%d of %d", i+1, endIndex, totalPhotos)
		
		for _, photo := range batch {
			if config.DryRun {
				log.Printf("[DRY RUN] Would send message for photo ID: %d (current sizes: %v)", 
						   photo.ID, photo.ResizedSizes)
				successCount++
			} else {
				message := QueueMessage{ID: photo.ID}
				err := publisher.PublishMessage(message)
				if err != nil {
					log.Printf("Error sending message for photo ID %d: %v", photo.ID, err)
					errorCount++
				} else {
					log.Printf("Sent reprocessing message for photo ID: %d", photo.ID)
					successCount++
				}
			}
		}
		
		// Delay between batches
		if endIndex < totalPhotos && config.DelayMs > 0 {
			log.Printf("Waiting %dms before next batch...", config.DelayMs)
			time.Sleep(time.Duration(config.DelayMs) * time.Millisecond)
		}
	}
	
	log.Printf("Processing completed - successful: %d, errors: %d", successCount, errorCount)
	return nil
}

type RabbitMQPublisher struct {
	connection *amqp.Connection
	channel    *amqp.Channel
	exchange   string
	routingKey string
}

func NewRabbitMQPublisher(url, exchangeName, routingKey, queueName string) (*RabbitMQPublisher, error) {
	conn, err := amqp.Dial(url)
	if err != nil {
		return nil, fmt.Errorf("failed to connect to RabbitMQ: %w", err)
	}
	
	ch, err := conn.Channel()
	if err != nil {
		conn.Close()
		return nil, fmt.Errorf("failed to open channel: %w", err)
	}
	
	// Declare exchange
	err = ch.ExchangeDeclare(
		exchangeName,
		"topic", // type
		true,    // durable
		false,   // auto-deleted
		false,   // internal
		false,   // no-wait
		nil,     // arguments
	)
	if err != nil {
		ch.Close()
		conn.Close()
		return nil, fmt.Errorf("failed to declare exchange: %w", err)
	}
	
	// Declare queue
	_, err = ch.QueueDeclare(
		queueName,
		true,  // durable
		false, // delete when unused
		false, // exclusive
		false, // no-wait
		nil,   // arguments
	)
	if err != nil {
		ch.Close()
		conn.Close()
		return nil, fmt.Errorf("failed to declare queue: %w", err)
	}
	
	// Bind queue to exchange
	err = ch.QueueBind(
		queueName,    // queue name
		routingKey,   // routing key
		exchangeName, // exchange
		false,
		nil,
	)
	if err != nil {
		ch.Close()
		conn.Close()
		return nil, fmt.Errorf("failed to bind queue: %w", err)
	}
	
	log.Println("Successfully connected to RabbitMQ")
	
	return &RabbitMQPublisher{
		connection: conn,
		channel:    ch,
		exchange:   exchangeName,
		routingKey: routingKey,
	}, nil
}

func (p *RabbitMQPublisher) PublishMessage(message QueueMessage) error {
	body, err := json.Marshal(message)
	if err != nil {
		return fmt.Errorf("failed to marshal message: %w", err)
	}
	
	err = p.channel.Publish(
		p.exchange,   // exchange
		p.routingKey, // routing key
		false,        // mandatory
		false,        // immediate
		amqp.Publishing{
			ContentType:  "application/json",
			Body:         body,
			DeliveryMode: amqp.Persistent, // make it persistent
		})
	
	if err != nil {
		return fmt.Errorf("failed to publish message: %w", err)
	}
	
	return nil
}

func (p *RabbitMQPublisher) Close() error {
	if p.channel != nil {
		p.channel.Close()
	}
	if p.connection != nil {
		p.connection.Close()
	}
	return nil
}