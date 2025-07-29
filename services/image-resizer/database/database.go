package database

import (
	"database/sql"
	"encoding/json"
	"fmt"

	_ "github.com/go-sql-driver/mysql"
)

type StoryPhoto struct {
	ID           int    `json:"id"`
	Url          string `json:"url"`
	ResizedSizes []int  `json:"resized_sizes"`
}

type Database struct {
	db *sql.DB
}

func NewDatabase(databaseURL string) (*Database, error) {
	db, err := sql.Open("mysql", databaseURL)
	if err != nil {
		return nil, fmt.Errorf("failed to connect to database: %w", err)
	}

	if err := db.Ping(); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	return &Database{db: db}, nil
}

func (d *Database) GetStoryPhoto(id int) (*StoryPhoto, error) {
	var photo StoryPhoto
	var resizedSizesJSON []byte

	query := `SELECT id, url, resized_sizes FROM story_photo WHERE id = ?`
	err := d.db.QueryRow(query, id).Scan(&photo.ID, &photo.Url, &resizedSizesJSON)
	if err != nil {
		return nil, fmt.Errorf("failed to get story photo: %w", err)
	}

	if len(resizedSizesJSON) > 0 {
		if err := json.Unmarshal(resizedSizesJSON, &photo.ResizedSizes); err != nil {
			return nil, fmt.Errorf("failed to unmarshal resized sizes: %w", err)
		}
	}

	return &photo, nil
}

func (d *Database) UpdateResizedSizes(id int, resizedSizes []int) error {
	resizedSizesJSON, err := json.Marshal(resizedSizes)
	if err != nil {
		return fmt.Errorf("failed to marshal resized sizes: %w", err)
	}

	query := `UPDATE story_photo SET resized_sizes = ? WHERE id = ?`
	_, err = d.db.Exec(query, resizedSizesJSON, id)
	if err != nil {
		return fmt.Errorf("failed to update resized sizes: %w", err)
	}

	return nil
}

func (d *Database) Close() error {
	return d.db.Close()
}
