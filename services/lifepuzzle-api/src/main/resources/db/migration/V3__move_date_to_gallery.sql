-- Move date field from Story to Gallery
-- Step 1: Add date column to gallery table
ALTER TABLE `gallery`
ADD COLUMN `date` date COMMENT '갤러리 날짜' AFTER `age_group`;

-- Step 2: Migrate existing dates from story to gallery through story_gallery relationship
-- For each gallery, set the date from the associated story (using MIN to handle multiple stories)
UPDATE `gallery` g
INNER JOIN (
    SELECT sg.gallery_id, MIN(s.date) as story_date
    FROM `story_gallery` sg
    INNER JOIN `story` s ON sg.story_id = s.id
    WHERE s.date IS NOT NULL
    GROUP BY sg.gallery_id
) AS story_dates ON g.id = story_dates.gallery_id
SET g.date = story_dates.story_date
WHERE g.date IS NULL;

-- Step 3: Remove old index that included date (before dropping the column)
ALTER TABLE `story` DROP INDEX `idx_story_hero_date`;

-- Step 4: Drop date column from story table
ALTER TABLE `story` DROP COLUMN `date`;

-- Step 5: Add new index for story without date
ALTER TABLE `story` ADD INDEX `idx_story_hero` (`hero_id`);
