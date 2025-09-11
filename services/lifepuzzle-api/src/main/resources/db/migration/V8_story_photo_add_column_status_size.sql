ALTER TABLE `story_photo`
ADD COLUMN status ENUM('pending', 'uploaded', 'failed') NOT NULL DEFAULT 'pending' COMMENT '사진 업로드 상태',
ADD COLUMN size INT NULL COMMENT '사진 크기';