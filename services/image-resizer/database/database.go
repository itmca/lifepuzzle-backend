package database

import (
	"database/sql"
	"encoding/json"
	"fmt"
	"log"

	_ "github.com/go-sql-driver/mysql"
)

type StoryPhoto struct {
	ID           int    `json:"id"`
	HeroID       int    `json:"hero_id"`
	Url          string `json:"url"`
	ResizedSizes []int  `json:"resized_sizes"`
}

type Database struct {
	db *sql.DB
}

func NewDatabase(databaseURL string) (*Database, error) {
	log.Printf("Connecting to database with URL pattern: mysql://[user:password]@[host]/[database]")
	
	db, err := sql.Open("mysql", databaseURL)
	if err != nil {
		return nil, fmt.Errorf("failed to connect to database: %w", err)
	}

	if err := db.Ping(); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	log.Printf("Successfully connected to database")
	return &Database{db: db}, nil
}

func (d *Database) GetStoryPhoto(id int) (*StoryPhoto, error) {
	log.Printf("Executing query to get story photo with ID: %d", id)
	
	var photo StoryPhoto
	var resizedSizesJSON []byte

	query := `SELECT id, hero_id, url, resized_sizes FROM story_photo WHERE id = ?`
	log.Printf("Executing SQL query: %s with parameters: [%d]", query, id)
	
	err := d.db.QueryRow(query, id).Scan(&photo.ID, &photo.HeroID, &photo.Url, &resizedSizesJSON)
	if err != nil {
		if err == sql.ErrNoRows {
			log.Printf("No story photo found with ID: %d", id)
			return nil, fmt.Errorf("story photo with ID %d not found: %w", id, err)
		}
		log.Printf("Database query error for photo ID %d: %v", id, err)
		return nil, fmt.Errorf("failed to get story photo: %w", err)
	}

	log.Printf("Successfully retrieved photo ID: %d, Hero ID: %d, URL: %s", photo.ID, photo.HeroID, photo.Url)

	if len(resizedSizesJSON) > 0 {
		if err := json.Unmarshal(resizedSizesJSON, &photo.ResizedSizes); err != nil {
			log.Printf("Failed to unmarshal resized sizes for photo ID %d: %v", id, err)
			return nil, fmt.Errorf("failed to unmarshal resized sizes: %w", err)
		}
		log.Printf("Photo ID %d has resized sizes: %v", id, photo.ResizedSizes)
	} else {
		log.Printf("Photo ID %d has no existing resized sizes", id)
	}

	return &photo, nil
}

func (d *Database) UpdateResizedSizes(id int, resizedSizes []int) error {
	log.Printf("Updating resized sizes for photo ID %d: %v", id, resizedSizes)
	
	resizedSizesJSON, err := json.Marshal(resizedSizes)
	if err != nil {
		log.Printf("Failed to marshal resized sizes for photo ID %d: %v", id, err)
		return fmt.Errorf("failed to marshal resized sizes: %w", err)
	}

	query := `UPDATE story_photo SET resized_sizes = ? WHERE id = ?`
	log.Printf("Executing SQL update: %s with parameters: [%s, %d]", query, string(resizedSizesJSON), id)
	
	result, err := d.db.Exec(query, resizedSizesJSON, id)
	if err != nil {
		log.Printf("Database update error for photo ID %d: %v", id, err)
		return fmt.Errorf("failed to update resized sizes: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	log.Printf("Successfully updated photo ID %d, rows affected: %d", id, rowsAffected)

	return nil
}

func (d *Database) UpdateStoryPhotoUrl(id int, url string) error {
	log.Printf("Updating URL for photo ID %d: %s", id, url)
	
	query := `UPDATE story_photo SET url = ? WHERE id = ?`
	log.Printf("Executing SQL update: %s with parameters: [%s, %d]", query, url, id)
	
	result, err := d.db.Exec(query, url, id)
	if err != nil {
		log.Printf("Database update error for photo ID %d: %v", id, err)
		return fmt.Errorf("failed to update photo URL: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	log.Printf("Successfully updated photo URL for ID %d, rows affected: %d", id, rowsAffected)

	return nil
}

func (d *Database) Close() error {
	return d.db.Close()
}
