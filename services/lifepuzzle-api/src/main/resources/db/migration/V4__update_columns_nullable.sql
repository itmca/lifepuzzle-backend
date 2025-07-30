ALTER TABLE `story`
MODIFY COLUMN `rec_question_id` bigint NULL COMMENT '추천 질문 번호',
MODIFY COLUMN `is_question_modified` tinyint(1) NULL COMMENT '추천 질문 수정 여부';