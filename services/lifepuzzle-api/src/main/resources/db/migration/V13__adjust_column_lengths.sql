-- Adjust column lengths for better data capacity
-- Increase email column length to accommodate longer email addresses
ALTER TABLE `user`
MODIFY COLUMN `email` VARCHAR(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '이메일';

-- Increase kakao_id column length to accommodate longer kakao IDs
ALTER TABLE `user`
MODIFY COLUMN `kakao_id` VARCHAR(64) DEFAULT NULL COMMENT 'kakao id';

-- Fix likes.target_id length to match story.id length
ALTER TABLE `likes`
MODIFY COLUMN `target_id` VARCHAR(32) NOT NULL COMMENT '대상 ID';

-- Increase user_hero_auth.auth column length for more detailed permissions
ALTER TABLE `user_hero_auth`
MODIFY COLUMN `auth` VARCHAR(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '권한';

-- Increase user_hero_share.auth column length for consistency
ALTER TABLE `user_hero_share`
MODIFY COLUMN `auth` VARCHAR(32) NOT NULL COMMENT '공유권한';