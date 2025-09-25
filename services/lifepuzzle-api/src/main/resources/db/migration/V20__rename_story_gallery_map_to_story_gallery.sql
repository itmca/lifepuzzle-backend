-- Rename story_gallery_map table to story_gallery for better naming convention
-- The table represents the relationship between Story and Gallery entities
-- Removing "_map" suffix makes the name more concise and intuitive

RENAME TABLE `story_gallery_map` TO `story_gallery`;