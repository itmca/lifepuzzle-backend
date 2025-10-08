-- AI 생성 비디오 테이블에 hero_no 컬럼 추가
ALTER TABLE `ai_generated_video`
ADD COLUMN `hero_no` BIGINT NOT NULL COMMENT '주인공 식별자' AFTER `id`;

-- hero_no를 기준으로 조회할 인덱스 추가
CREATE INDEX `idx_ai_generated_video_hero_no` ON `ai_generated_video` (`hero_no`, `deleted_at`);

-- 기존 gallery_id 기준 인덱스는 유지 (갤러리별 조회도 필요할 수 있음)