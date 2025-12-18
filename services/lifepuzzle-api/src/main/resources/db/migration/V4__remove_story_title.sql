-- Remove title field from Story and merge it into content
-- Step 1: Merge title into content (title becomes first line if it exists)
UPDATE `story`
SET content = CASE
    WHEN title IS NOT NULL AND title != '' AND content IS NOT NULL THEN CONCAT(title, '\n', content)
    WHEN title IS NOT NULL AND title != '' AND content IS NULL THEN title
    ELSE content
END
WHERE title IS NOT NULL AND title != '';

-- Step 2: Drop title column from story table
ALTER TABLE `story` DROP COLUMN `title`;
