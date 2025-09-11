-- Gallery API Performance Optimization Indexes

-- Index for hero_id lookup (primary lookup column)
CREATE INDEX idx_story_photo_hero_id ON story_photo(hero_id);

-- Composite index for hero_id and age_group filtering  
CREATE INDEX idx_story_photo_hero_age ON story_photo(hero_id, age_group);

-- Index for story_id lookup in story_photo_map (for JOIN with story table)
CREATE INDEX idx_story_photo_map_story_id ON story_photo_map(story_id);