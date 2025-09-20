CREATE TABLE `ai_driving_video` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '드라이빙 비디오 ID',
  `name` VARCHAR(100) NOT NULL COMMENT '드라이빙 비디오 이름',
  `url` VARCHAR(500) NOT NULL COMMENT '드라이빙 비디오 URL',
  `thumbnail_url` VARCHAR(500) COMMENT '썸네일 이미지 URL',
  `description` TEXT COMMENT '드라이빙 비디오 설명',
  `priority` INT NOT NULL DEFAULT 0 COMMENT '우선순위 (높을수록 우선)',
  `deleted_at` TIMESTAMP NULL COMMENT '삭제일시',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
) COMMENT 'AI 드라이빙 비디오 테이블';

CREATE INDEX `idx_ai_driving_video_priority_created` ON `ai_driving_video` (`deleted_at`, `priority` DESC, `created_at` DESC);