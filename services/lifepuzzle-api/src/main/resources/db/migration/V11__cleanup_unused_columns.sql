-- Remove unused columns from hero table
ALTER TABLE `hero` DROP COLUMN `deleted`;
ALTER TABLE `hero` DROP COLUMN `parent_id`;
ALTER TABLE `hero` DROP COLUMN `spouse_id`;

-- Remove unused columns from story table
ALTER TABLE `story` DROP COLUMN `image_folder`;
ALTER TABLE `story` DROP COLUMN `image_files`;
ALTER TABLE `story` DROP COLUMN `video_folder`;
ALTER TABLE `story` DROP COLUMN `video_files`;

-- Update user table columns
ALTER TABLE `user` CHANGE COLUMN `validated` `email_validated` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '이메일 검증 여부';
ALTER TABLE `user` DROP COLUMN `email_notice`;
ALTER TABLE `user` DROP COLUMN `phone_notice`;
ALTER TABLE `user` DROP COLUMN `kakao_notice`;
ALTER TABLE `user` DROP COLUMN `inapp_notice`;
ALTER TABLE `user` ADD COLUMN `push_opt_in` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '푸시 알림 수신 동의' AFTER `email_validated`;