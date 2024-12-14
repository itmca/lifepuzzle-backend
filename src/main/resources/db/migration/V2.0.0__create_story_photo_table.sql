CREATE TABLE `story_photo` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '사진 ID',
    `hero_id` bigint NOT NULL COMMENT '주인공 ID',
    `url` varchar(512) NOT NULL COMMENT '사진 URL',
    `age_group` varchar(32) NOT NULL COMMENT '나이대',
    `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
    `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='스토리 사진';

CREATE TABLE `story_photo_map` (
    `photo_id` bigint NOT NULL COMMENT '사진 ID',
    `story_id` varchar(32) NOT NULL COMMENT '스토리 ID',
    `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
    PRIMARY KEY (`photo_id`, `story_id`)
) ENGINE=InnoDB CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='스토리와 사진 매핑';