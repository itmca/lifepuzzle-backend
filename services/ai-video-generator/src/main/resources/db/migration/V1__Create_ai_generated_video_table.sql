CREATE TABLE IF NOT EXISTS ai_generated_video (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hero_no BIGINT NOT NULL,
    gallery_id BIGINT NOT NULL,
    driving_video_id BIGINT NOT NULL,
    video_url VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    started_at DATETIME(6),
    completed_at DATETIME(6),
    error_message TEXT,
    deleted_at DATETIME(6),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    
    INDEX idx_hero_no (hero_no),
    INDEX idx_status (status),
    INDEX idx_gallery_id (gallery_id),
    INDEX idx_driving_video_id (driving_video_id),
    UNIQUE KEY uk_hero_gallery_driving_video (hero_no, gallery_id, driving_video_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;