package io.itmca.lifepuzzle.domain.question.endpoint.response;

import io.itmca.lifepuzzle.domain.question.entity.Question;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionQueryResponse {

    private RecommendQuestionDTO recommendQuestions;

    public static QuestionQueryResponse from(Question questions) {
        return QuestionQueryResponse.builder()
                .recommendQuestions(RecommendQuestionDTO.from(questions))
                .build();
    }

}
