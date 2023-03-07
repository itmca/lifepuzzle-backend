package io.itmca.lifepuzzle.domain.question.endpoint.response;

import io.itmca.lifepuzzle.domain.question.entity.Question;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public
class RecommendQuestionDTO {
    final Long questionNo;
    final String category;
    final String question;

    public static RecommendQuestionDTO from(Question question) {
        return RecommendQuestionDTO.builder()
                .questionNo(question.getQuestionNo())
                .category(question.getCategory())
                .question(question.getQuestion())
                .build();
    }
}
