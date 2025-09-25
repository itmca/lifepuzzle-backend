-- Fix data type consistency issues
-- Change user_hero_auth table columns to BIGINT to match other tables
ALTER TABLE `user_hero_auth`
MODIFY COLUMN `user_id` BIGINT NOT NULL COMMENT '유저키',
MODIFY COLUMN `hero_id` BIGINT NOT NULL COMMENT '주인공키';

-- Change user_hero_share table columns to BIGINT to match other tables
ALTER TABLE `user_hero_share`
MODIFY COLUMN `sharer_user_id` BIGINT NOT NULL COMMENT '공유자번호',
MODIFY COLUMN `hero_id` BIGINT NOT NULL COMMENT '주인공번호';