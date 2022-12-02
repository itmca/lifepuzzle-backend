package io.itmca.lifepuzzle.domain.question.service;

import io.itmca.lifepuzzle.domain.question.repository.QuestionQueryRepository;
import io.itmca.lifepuzzle.domain.question.repository.QuestionStoryQueryRepository;

public class QuestionQueryService {
    private final QuestionQueryRepository questionQueryRepository;
    private final QuestionStoryQueryRepository questionStoryQueryRepository;

    public QuestionQueryService(QuestionQueryRepository questionQueryRepository, QuestionStoryQueryRepository questionStoryQueryRepository) {
        this.questionQueryRepository = questionQueryRepository;
        this.questionStoryQueryRepository = questionStoryQueryRepository;
    }

    public void getRecommendedQuestion(String category, int herNo, int size){};
    public void getUsedQuestionsOfHero(int herNo){};

}
