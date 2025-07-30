CREATE TABLE `comment`
(
    `id`         bigint      NOT NULL AUTO_INCREMENT COMMENT '댓글 ID',
    `story_id`   varchar(32) NOT NULL COMMENT '스토리 ID',
    `writer_id`  bigint      NOT NULL COMMENT '작성자 회원 ID',
    `content`    text COMMENT '댓글 내용',
    `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
    `updated_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '수정일자',
    PRIMARY KEY (`id`),
    KEY          `idx_storyid` (`story_id`),
    KEY          `idx_writerid` (`writer_id`)
) COMMENT='댓글 정보';

CREATE TABLE `hero`
(
    `id`         bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '주인공 ID',
    `parent_id`  bigint       DEFAULT '0' COMMENT '부모키',
    `spouse_id`  bigint       DEFAULT '0' COMMENT '배우자키',
    `name`       varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '이름',
    `nickname`   varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '별칭',
    `birthday`   date                                                         NOT NULL COMMENT '생일',
    `image`      varchar(128) DEFAULT NULL COMMENT '대표이미지',
    `title`      varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '대표제목',
    `created_at` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
    `updated_at` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '수정일자',
    `deleted`    tinyint(1) DEFAULT '0' COMMENT '삭제 여부',
    `deleted_at` datetime     DEFAULT NULL COMMENT '삭제된 시점',
    PRIMARY KEY (`id`)
) COMMENT='주인공 정보';

CREATE TABLE `likes`
(
    `id`         bigint      NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `user_id`    bigint      NOT NULL COMMENT '유저 ID',
    `type`       varchar(20) NOT NULL COMMENT '대상 타입 (STORY, COMMENT)',
    `target_id`  varchar(20) NOT NULL COMMENT '대상 ID',
    `created_at` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_userid_type_targetid` (`user_id`,`type`,`target_id`),
    KEY          `idx_type_targetid` (`type`,`target_id`)
) COMMENT='좋아요';

CREATE TABLE `question`
(
    `id`               bigint NOT NULL AUTO_INCREMENT COMMENT '질문 ID',
    `category`         varchar(32)  DEFAULT NULL COMMENT '카테고리',
    `question_content` varchar(128) DEFAULT NULL COMMENT '질문내용',
    `use_count`        int    NOT NULL DEFAULT 0 COMMENT '사용횟수',
    `question_grade`   float  NOT NULL DEFAULT 0 COMMENT '질문평점',
    `created_at`       datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
    PRIMARY KEY (`id`)
) COMMENT='질문 정보';

CREATE TABLE `story`
(
    `id`                   varchar(32)                                                  NOT NULL COMMENT '스토리 ID',
    `user_id`              bigint                                                       NOT NULL COMMENT '유저번호',
    `hero_id`              bigint                                                       NOT NULL COMMENT '주인공키',
    `rec_question_id`      bigint                                                       NOT NULL COMMENT '추천 질문 번호',
    `used_question`        text COMMENT '사용된 질문',
    `is_question_modified` tinyint(1) NOT NULL COMMENT '추천 질문 수정 여부',
    `title`                varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '제목',
    `content`              text COMMENT '메인글 내용',
    `image_folder`         varchar(128)                                                          DEFAULT NULL COMMENT '이미지 폴더 위치',
    `image_files`          text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '이미지 파일',
    `audio_folder`         varchar(128)                                                          DEFAULT NULL COMMENT '오디오위치',
    `audio_files`          text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '오디오 파일',
    `video_folder`         varchar(128)                                                          DEFAULT NULL,
    `video_files`          text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
    `date`                 date                                                         NOT NULL COMMENT '대상시점',
    `created_at`           datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성시점',
    `updated_at`           datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정시점',
    `hashtag`              varchar(128)                                                          DEFAULT NULL COMMENT '해시태그',
    PRIMARY KEY (`id`),
    KEY                    `idx_heroid` (`hero_id`),
    KEY                    `idx_recquestionid` (`rec_question_id`)
) COMMENT='스토리 정보';

CREATE TABLE `user`
(
    `id`           bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '회원 식별 ID',
    `login_id`     varchar(64)                                                  NOT NULL COMMENT '로그인 및 화면 노출에 사용되는 ID',
    `email`        varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT NULL COMMENT '이메일',
    `salt`         varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT NULL COMMENT 'SALT',
    `password`     varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci         DEFAULT NULL COMMENT '비밀번호',
    `validated`    tinyint(1) DEFAULT NULL COMMENT '이메일 인증 여부',
    `nick_name`    varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '별명',
    `birthday`     date                                                                  DEFAULT NULL COMMENT '태어난 날',
    `recent_hero`  int                                                                   DEFAULT '0' COMMENT '최근 작성한 주인공',
    `image`        varchar(128)                                                          DEFAULT NULL,
    `kakao_id`     varchar(16)                                                           DEFAULT NULL COMMENT 'kakao id',
    `apple_id`     varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci          DEFAULT NULL COMMENT 'apple id',
    `email_notice` tinyint(1) DEFAULT NULL COMMENT '메일알림',
    `phone_notice` tinyint(1) DEFAULT NULL COMMENT '휴대폰알림',
    `kakao_notice` tinyint(1) DEFAULT NULL COMMENT '카카오톡알림',
    `inapp_notice` tinyint(1) DEFAULT NULL COMMENT '인앱알림',
    `created_at`   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
    `updated_at`   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '수정일자',
    PRIMARY KEY (`id`),
    UNIQUE KEY uk_loginid (`login_id`)
) COMMENT='유저 기본 정보';

CREATE TABLE `user_hero_auth`
(
    `id`         int      NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `user_id`    int      NOT NULL COMMENT '유저키',
    `hero_id`    int      NOT NULL COMMENT '주인공키',
    `auth`       varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '권한',
    `created_at` datetime NOT NULL                                            DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
    `updated_at` datetime NOT NULL                                            DEFAULT CURRENT_TIMESTAMP COMMENT '수정일자',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_userid_heroid` (`user_id`,`hero_id`)
) COMMENT='유저 주인공 권한';

CREATE TABLE `user_hero_share`
(
    `id`             varchar(16) NOT NULL COMMENT 'PK',
    `sharer_user_id` int         NOT NULL COMMENT '공유자번호',
    `hero_id`        int         NOT NULL COMMENT '주인공번호',
    `auth`           varchar(16) NOT NULL COMMENT '공유권한',
    `created_at`     datetime DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
    `expired_at`     datetime DEFAULT CURRENT_TIMESTAMP COMMENT '만료일자',
    PRIMARY KEY (`id`)
) COMMENT='주인공 공유 정보';
