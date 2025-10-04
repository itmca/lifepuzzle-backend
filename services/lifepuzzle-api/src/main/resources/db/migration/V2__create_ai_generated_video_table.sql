CREATE TABLE `ai_generated_video` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'AI 생성 비디오 ID',
  `gallery_id` BIGINT NOT NULL COMMENT '갤러리(이미지) ID',
  `driving_video_id` BIGINT NOT NULL COMMENT '드라이빙 비디오 ID',
  `video_url` VARCHAR(500) COMMENT '생성된 비디오 URL',
  `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '생성 상태 (PENDING, IN_PROGRESS, COMPLETED, FAILED)',
  `started_at` TIMESTAMP NULL COMMENT '생성 시작 시점',
  `completed_at` TIMESTAMP NULL COMMENT '생성 완료 시점',
  `error_message` TEXT COMMENT '실패 시 오류 메시지',
  `deleted_at` TIMESTAMP NULL COMMENT '삭제일시',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
  
  FOREIGN KEY (`gallery_id`) REFERENCES `gallery` (`id`),
  FOREIGN KEY (`driving_video_id`) REFERENCES `ai_driving_video` (`id`)
) COMMENT 'AI 생성 비디오 테이블';

CREATE INDEX `idx_ai_generated_video_gallery` ON `ai_generated_video` (`gallery_id`, `deleted_at`);
CREATE INDEX `idx_ai_generated_video_status_created` ON `ai_generated_video` (`status`, `created_at` DESC);