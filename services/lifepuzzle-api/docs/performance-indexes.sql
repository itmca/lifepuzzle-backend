-- Gallery API Performance Optimization Indexes
-- Execute these manually on the production database

-- Index for hero_id lookup (primary lookup column)
CREATE INDEX idx_story_photo_hero_id ON story_photo(hero_id);

-- Composite index for hero_id and age_group filtering  
CREATE INDEX idx_story_photo_hero_age ON story_photo(hero_id, age_group);

-- Index for story_gallery table to optimize story mapping
CREATE INDEX idx_story_gallery_photo_id ON story_gallery(photo_id);
CREATE INDEX idx_story_gallery_story_id ON story_gallery(story_id);