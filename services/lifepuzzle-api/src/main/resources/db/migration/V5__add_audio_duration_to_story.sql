-- Add audio_duration column to story table
ALTER TABLE `story`
ADD COLUMN `audio_duration` INT NULL COMMENT '음성 파일 재생 시간(초)';
