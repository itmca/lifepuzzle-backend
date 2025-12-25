-- Sync story table schema with production database
-- This migration adds missing columns that exist in production but not in V1-V5 migrations
-- Note: Production DB already has these columns, this ensures schema consistency for new deployments

-- Step 1: Add rec_question_id column if not exists
SET @col_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'story'
      AND COLUMN_NAME = 'rec_question_id'
);

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE `story` ADD COLUMN `rec_question_id` bigint DEFAULT NULL COMMENT ''추천 질문 번호'' AFTER `hero_id`',
    'SELECT "Column rec_question_id already exists" AS message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 2: Add used_question column if not exists
SET @col_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'story'
      AND COLUMN_NAME = 'used_question'
);

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE `story` ADD COLUMN `used_question` text COMMENT ''사용된 질문'' AFTER `rec_question_id`',
    'SELECT "Column used_question already exists" AS message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 3: Add is_question_modified column if not exists
SET @col_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'story'
      AND COLUMN_NAME = 'is_question_modified'
);

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE `story` ADD COLUMN `is_question_modified` tinyint(1) DEFAULT NULL COMMENT ''추천 질문 수정 여부'' AFTER `used_question`',
    'SELECT "Column is_question_modified already exists" AS message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 4: Add audio_folder column if not exists
SET @col_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'story'
      AND COLUMN_NAME = 'audio_folder'
);

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE `story` ADD COLUMN `audio_folder` varchar(128) DEFAULT NULL COMMENT ''오디오위치'' AFTER `content`',
    'SELECT "Column audio_folder already exists" AS message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 5: Add audio_files column if not exists
SET @col_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'story'
      AND COLUMN_NAME = 'audio_files'
);

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE `story` ADD COLUMN `audio_files` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT ''오디오 파일'' AFTER `audio_folder`',
    'SELECT "Column audio_files already exists" AS message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 6: Rename writer_id to user_id if writer_id exists
SET @col_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'story'
      AND COLUMN_NAME = 'writer_id'
);

SET @sql = IF(@col_exists > 0,
    'ALTER TABLE `story` CHANGE COLUMN `writer_id` `user_id` bigint NOT NULL COMMENT ''유저번호''',
    'SELECT "Column writer_id does not exist, skip rename" AS message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 7: Drop age column if exists (no longer used)
SET @col_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'story'
      AND COLUMN_NAME = 'age'
);

SET @sql = IF(@col_exists > 0,
    'ALTER TABLE `story` DROP COLUMN `age`',
    'SELECT "Column age does not exist" AS message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 8: Add indexes if not exists
SET @index_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'story'
      AND INDEX_NAME = 'idx_heroid'
);

SET @sql = IF(@index_exists = 0,
    'ALTER TABLE `story` ADD INDEX `idx_heroid` (`hero_id`)',
    'SELECT "Index idx_heroid already exists" AS message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @index_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'story'
      AND INDEX_NAME = 'idx_recquestionid'
);

SET @sql = IF(@index_exists = 0,
    'ALTER TABLE `story` ADD INDEX `idx_recquestionid` (`rec_question_id`)',
    'SELECT "Index idx_recquestionid already exists" AS message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @index_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'story'
      AND INDEX_NAME = 'idx_story_user_date'
);

SET @sql = IF(@index_exists = 0,
    'ALTER TABLE `story` ADD INDEX `idx_story_user_date` (`user_id`)',
    'SELECT "Index idx_story_user_date already exists" AS message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
