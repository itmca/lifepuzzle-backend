-- Reorder table columns to follow standard conventions:
-- 1. Primary Key (id)
-- 2. Foreign Keys
-- 3. Business Logic columns
-- 4. Metadata columns (created_at, updated_at, deleted_at)

-- Hero table: Move deleted_at after updated_at
ALTER TABLE `hero`
MODIFY COLUMN `deleted_at` DATETIME DEFAULT NULL COMMENT '삭제된 시점'
AFTER `updated_at`;

-- Story table: Move hashtag before created_at (business logic before metadata)
ALTER TABLE `story`
MODIFY COLUMN `hashtag` VARCHAR(128) DEFAULT NULL COMMENT '해시태그'
AFTER `date`;

-- User table: Reorganize to group related columns
-- Move all OAuth/auth related columns together
ALTER TABLE `user`
MODIFY COLUMN `kakao_id` VARCHAR(64) DEFAULT NULL COMMENT 'kakao id'
AFTER `apple_id`;

-- Move image after nick_name (profile related columns together)
ALTER TABLE `user`
MODIFY COLUMN `image` VARCHAR(128) DEFAULT NULL COMMENT '프로필 이미지'
AFTER `nick_name`;

-- Move recent_hero after birthday (user basic info together)
ALTER TABLE `user`
MODIFY COLUMN `recent_hero` BIGINT DEFAULT 0 COMMENT '최근 작성한 주인공'
AFTER `birthday`;

-- Move push_opt_in before created_at (business logic before metadata)
ALTER TABLE `user`
MODIFY COLUMN `push_opt_in` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '푸시 알림 수신 동의'
AFTER `image`;

-- User_hero_share table: Move expired_at before created_at (logical time order)
ALTER TABLE `user_hero_share`
MODIFY COLUMN `expired_at` DATETIME DEFAULT NULL COMMENT '만료일자'
AFTER `auth`,
MODIFY COLUMN `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자'
AFTER `expired_at`;