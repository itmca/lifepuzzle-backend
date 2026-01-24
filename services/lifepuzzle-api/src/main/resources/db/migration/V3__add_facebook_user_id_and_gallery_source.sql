ALTER TABLE `hero`
  ADD COLUMN `facebook_user_id` varchar(128) DEFAULT NULL COMMENT '페이스북 사용자 ID',
  ADD UNIQUE KEY `uk_hero_facebook_user_id` (`facebook_user_id`);

ALTER TABLE `gallery`
  ADD COLUMN `source` varchar(32) NOT NULL DEFAULT 'UPLOAD' COMMENT '사진 출처',
  ADD COLUMN `uploaded_user_id` bigint DEFAULT NULL COMMENT '업로드한 사용자 ID',
  ADD KEY `idx_gallery_uploaded_user_id` (`uploaded_user_id`);
