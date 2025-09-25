-- Add performance indexes for better query optimization

-- Add composite index for comment table (common query pattern: get comments by story ordered by creation time)
CREATE INDEX `idx_comment_story_created` ON `comment` (`story_id`, `created_at` DESC);

-- Add composite index for story table (common query pattern: get stories by hero and date)
CREATE INDEX `idx_story_hero_date` ON `story` (`hero_id`, `date` DESC);
CREATE INDEX `idx_story_user_date` ON `story` (`user_id`, `date` DESC);
CREATE INDEX `idx_story_date` ON `story` (`date` DESC);

-- Add index for likes table to improve performance on target lookups
CREATE INDEX `idx_likes_target_type_id` ON `likes` (`type`, `target_id`);

-- Add index for user table email lookups (for login/registration)
CREATE INDEX `idx_user_email` ON `user` (`email`);
CREATE INDEX `idx_user_kakao_id` ON `user` (`kakao_id`);
CREATE INDEX `idx_user_apple_id` ON `user` (`apple_id`);

-- Add index for hero table to support soft delete queries
CREATE INDEX `idx_hero_deleted_at` ON `hero` (`deleted_at`);

-- Add composite index for user_hero_auth to improve permission checks
CREATE INDEX `idx_user_hero_auth_user_hero` ON `user_hero_auth` (`user_id`, `hero_id`);

-- Add composite index for user_hero_share to improve sharing queries
CREATE INDEX `idx_user_hero_share_hero_expired` ON `user_hero_share` (`hero_id`, `expired_at` DESC);

-- Add index for question to support category and rating queries
CREATE INDEX `idx_question_category_grade` ON `question` (`category`, `question_grade` DESC);
CREATE INDEX `idx_question_use_count` ON `question` (`use_count` DESC);