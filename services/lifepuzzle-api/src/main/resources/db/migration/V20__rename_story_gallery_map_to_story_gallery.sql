-- Rename gallery_story_map table to story_gallery for better naming convention
-- The table represents the relationship between Story and Gallery entities
-- This standardizes the naming to follow entity_relationship pattern

-- Check if gallery_story_map exists and rename it
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES
   WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'gallery_story_map') > 0,
  'RENAME TABLE `gallery_story_map` TO `story_gallery`',
  'SELECT "gallery_story_map table does not exist - skipping rename"'
));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Also check if story_gallery_map exists and rename it (fallback)
SET @sql = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES
   WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'story_gallery_map') > 0,
  'RENAME TABLE `story_gallery_map` TO `story_gallery`',
  'SELECT "story_gallery_map table does not exist - skipping rename"'
));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;