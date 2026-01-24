-- LifePuzzle Database Schema

SET NAMES utf8mb4;

-- AI 드라이빙 비디오 테이블
CREATE TABLE `ai_driving_video` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '드라이빙 비디오 ID',
  `name` varchar(100) NOT NULL COMMENT '드라이빙 비디오 이름',
  `url` varchar(500) NOT NULL COMMENT '드라이빙 비디오 URL',
  `thumbnail_url` varchar(500) DEFAULT NULL COMMENT '썸네일 이미지 URL',
  `description` text COMMENT '드라이빙 비디오 설명',
  `priority` int NOT NULL DEFAULT '0' COMMENT '우선순위 (높을수록 우선)',
  `deleted_at` datetime DEFAULT NULL COMMENT '삭제일시',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
  PRIMARY KEY (`id`),
  KEY `idx_ai_driving_video_priority_created` (`deleted_at`,`priority` DESC,`created_at` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AI 드라이빙 비디오 테이블';

-- AI 생성 비디오 테이블
CREATE TABLE `ai_generated_video` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'AI 생성 비디오 ID',
  `hero_no` bigint NOT NULL COMMENT '주인공 식별자',
  `gallery_id` bigint NOT NULL COMMENT '갤러리(이미지) ID',
  `driving_video_id` bigint NOT NULL COMMENT '드라이빙 비디오 ID',
  `video_url` varchar(500) DEFAULT NULL COMMENT '생성된 비디오 URL',
  `status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '생성 상태 (PENDING, IN_PROGRESS, COMPLETED, FAILED)',
  `started_at` datetime DEFAULT NULL COMMENT '생성 시작 시점',
  `completed_at` datetime DEFAULT NULL COMMENT '생성 완료 시점',
  `error_message` text COMMENT '실패 시 오류 메시지',
  `deleted_at` datetime DEFAULT NULL COMMENT '삭제일시',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
  PRIMARY KEY (`id`),
  KEY `idx_ai_generated_video_hero_no` (`hero_no`,`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='AI 생성 비디오 테이블';

-- 댓글 정보
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '댓글 ID',
  `story_id` varchar(32) NOT NULL COMMENT '스토리 ID',
  `writer_id` bigint NOT NULL COMMENT '작성자 회원 ID',
  `content` text COMMENT '댓글 내용',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
  PRIMARY KEY (`id`),
  KEY `idx_comment_story_id` (`story_id`),
  KEY `idx_comment_writer_id` (`writer_id`),
  KEY `idx_comment_story_created` (`story_id`,`created_at` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='댓글 정보';

-- 갤러리 테이블
CREATE TABLE `gallery` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '사진 ID',
  `hero_id` bigint NOT NULL COMMENT '주인공 ID',
  `url` varchar(512) NOT NULL COMMENT '사진 URL',
  `age_group` varchar(32) NOT NULL COMMENT '나이대',
  `date` date DEFAULT NULL COMMENT '갤러리 날짜',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
  `type` varchar(32) NOT NULL COMMENT '파일 타입',
  `resized_sizes` text COMMENT '리사이징된 이미지 크기 목록',
  `status` varchar(40) NOT NULL DEFAULT 'PENDING' COMMENT '사진 업로드 상태',
  PRIMARY KEY (`id`),
  KEY `idx_gallery_hero_age_created` (`hero_id`,`age_group`,`created_at` DESC),
  KEY `idx_gallery_status_created` (`status`,`created_at` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='갤러리';

-- 주인공 테이블
CREATE TABLE `hero` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '주인공 ID',
  `name` varchar(32) NOT NULL COMMENT '이름',
  `nickname` varchar(32) NOT NULL COMMENT '별칭',
  `birthdate` date NOT NULL COMMENT '생일',
  `image` varchar(128) DEFAULT NULL COMMENT '대표이미지',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
  `is_lunar` tinyint(1) NOT NULL DEFAULT '0' COMMENT '음력 여부',
  `deleted_at` datetime DEFAULT NULL COMMENT '삭제된 시점',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='주인공 정보';

-- 좋아요 테이블
CREATE TABLE `content_like` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `user_id` bigint NOT NULL COMMENT '유저 ID',
  `type` varchar(20) NOT NULL COMMENT '대상 타입 (STORY, COMMENT)',
  `content_id` varchar(32) NOT NULL COMMENT '컨텐츠 ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_content_like_user_type_content` (`user_id`,`type`,`content_id`),
  KEY `idx_content_like_type_content` (`type`,`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='좋아요';

-- 질문 테이블
CREATE TABLE `question` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '질문 ID',
  `category` varchar(32) DEFAULT NULL COMMENT '카테고리',
  `question_content` varchar(128) DEFAULT NULL COMMENT '질문내용',
  `use_count` int NOT NULL DEFAULT '0' COMMENT '사용횟수',
  `question_grade` float NOT NULL DEFAULT '0' COMMENT '질문평점',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
  PRIMARY KEY (`id`),
  KEY `idx_question_category_grade` (`category`,`question_grade` DESC),
  KEY `idx_question_use_count` (`use_count` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='질문 정보';

-- 스토리 테이블
CREATE TABLE `story` (
  `id` varchar(32) NOT NULL COMMENT '스토리 ID',
  `hero_id` bigint NOT NULL COMMENT '주인공 ID',
  `writer_id` bigint NOT NULL COMMENT '작성자 회원 ID',
  `content` text COMMENT '메인글 내용',
  `audio_folder` varchar(128) DEFAULT NULL COMMENT '오디오 폴더 위치',
  `audio_files` mediumtext COMMENT '오디오 파일',
  `hashtag` varchar(128) DEFAULT NULL COMMENT '해시태그',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성시점',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정시점',
  PRIMARY KEY (`id`),
  KEY `idx_story_hero` (`hero_id`),
  KEY `idx_story_writer_created` (`writer_id`,`created_at` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='스토리 정보';

-- 스토리-갤러리 매핑 테이블
CREATE TABLE `story_gallery` (
  `story_id` varchar(32) NOT NULL COMMENT '스토리 ID',
  `gallery_id` bigint NOT NULL COMMENT '갤러리 ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
  PRIMARY KEY (`story_id`,`gallery_id`),
  KEY `idx_story_gallery_gallery` (`gallery_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='스토리-갤러리 매핑';

-- 회원 테이블
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '회원 ID',
  `login_id` varchar(64) NOT NULL COMMENT '로그인 ID',
  `email` varchar(256) DEFAULT NULL COMMENT '이메일',
  `salt` varchar(16) DEFAULT NULL COMMENT '솔트',
  `password` varchar(128) DEFAULT NULL COMMENT '비밀번호',
  `email_validated` tinyint(1) NOT NULL DEFAULT '0' COMMENT '이메일 검증 여부',
  `push_opt_in` tinyint(1) NOT NULL DEFAULT '0' COMMENT '푸시 알림 수신 동의',
  `nick_name` varchar(64) NOT NULL COMMENT '닉네임',
  `image` varchar(128) DEFAULT NULL COMMENT '프로필 이미지',
  `birthday` date DEFAULT NULL COMMENT '생일',
  `recent_hero` bigint DEFAULT '0' COMMENT '최근 작성한 주인공',
  `kakao_id` varchar(128) DEFAULT NULL COMMENT '카카오 ID',
  `apple_id` varchar(256) DEFAULT NULL COMMENT '애플 ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_login_id` (`login_id`),
  UNIQUE KEY `uk_user_email` (`email`),
  UNIQUE KEY `uk_user_kakao_id` (`kakao_id`),
  UNIQUE KEY `uk_user_apple_id` (`apple_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='회원 정보';

-- 회원-주인공 권한 테이블
CREATE TABLE `user_hero_auth` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `user_id` bigint NOT NULL COMMENT '회원 ID',
  `hero_id` bigint NOT NULL COMMENT '주인공 ID',
  `auth` varchar(32) DEFAULT NULL COMMENT '권한',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_hero_auth_user_hero` (`user_id`,`hero_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='회원-주인공 권한';

-- 회원-주인공 공유 테이블
CREATE TABLE `user_hero_share` (
  `id` varchar(16) NOT NULL COMMENT 'PK',
  `sharer_user_id` bigint NOT NULL COMMENT '공유자 번호',
  `hero_id` bigint NOT NULL COMMENT '주인공 번호',
  `auth` varchar(32) NOT NULL COMMENT '공유 권한',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
  `expired_at` datetime DEFAULT NULL COMMENT '만료일시',
  PRIMARY KEY (`id`),
  KEY `idx_user_hero_share_hero_expired` (`hero_id`,`expired_at` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='회원-주인공 공유';
