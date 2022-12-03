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

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    static class RecommendQuestionDTO {
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
}
