-- Remove unused columns from hero table
ALTER TABLE `hero` DROP COLUMN IF EXISTS `deleted`;
ALTER TABLE `hero` DROP COLUMN IF EXISTS `parent_id`;
ALTER TABLE `hero` DROP COLUMN IF EXISTS `spouse_id`;

-- Remove unused columns from story table
ALTER TABLE `story` DROP COLUMN IF EXISTS `image_folder`;
ALTER TABLE `story` DROP COLUMN IF EXISTS `image_files`;
ALTER TABLE `story` DROP COLUMN IF EXISTS `video_folder`;
ALTER TABLE `story` DROP COLUMN IF EXISTS `video_files`;

-- Update user table columns
ALTER TABLE `user` CHANGE COLUMN `validated` `email_validated` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '이메일 검증 여부';
ALTER TABLE `user` DROP COLUMN IF EXISTS `email_notice`;
ALTER TABLE `user` DROP COLUMN IF EXISTS `phone_notice`;
ALTER TABLE `user` DROP COLUMN IF EXISTS `kakao_notice`;
ALTER TABLE `user` DROP COLUMN IF EXISTS `inapp_notice`;
ALTER TABLE `user` ADD COLUMN `push_opt_in` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '푸시 알림 수신 동의' AFTER `email_validated`;