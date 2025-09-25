-- Rename story_photo table to gallery
RENAME TABLE `story_photo` TO `gallery`;

-- Rename story_photo_map table to gallery_story_map
RENAME TABLE `story_photo_map` TO `gallery_story_map`;

-- Rename column in gallery_story_map table from photo_id to gallery_id
ALTER TABLE `gallery_story_map` CHANGE COLUMN `photo_id` `gallery_id` BIGINT NOT NULL COMMENT '갤러리 ID';

-- Update comments
ALTER TABLE `gallery` COMMENT='갤러리';
ALTER TABLE `gallery_story_map` COMMENT='갤러리와 스토리 매핑';