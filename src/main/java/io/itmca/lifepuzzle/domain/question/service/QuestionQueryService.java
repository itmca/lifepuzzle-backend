package io.itmca.lifepuzzle.domain.question.service;

import io.itmca.lifepuzzle.domain.question.entity.Question;
import io.itmca.lifepuzzle.domain.question.repository.QuestionQueryRepository;
import io.itmca.lifepuzzle.domain.question.repository.QuestionStoryQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionQueryService {
    private final QuestionQueryRepository questionQueryRepository;
    private final QuestionStoryQueryRepository questionStoryQueryRepository;

    public List<Question> getRecommendedQuestion(String category, Long heroNo, Long size){
        var questions = ObjectUtils.isEmpty(category.trim())
                ? questionQueryRepository.findAll()
                : questionQueryRepository.findByCategory(category);
        var alreadyUsedQuestions = getUsedQuestionsOfHero(heroNo);

        return questions.stream()
                .filter(question -> !alreadyUsedQuestions.contains(question.getQuestionNo()))
                .limit(size)
                .toList();
    };

    private List<Long> getUsedQuestionsOfHero(Long heroNo){
        var heroQuestionStories = questionStoryQueryRepository.findByHeroNo(heroNo);

        return heroQuestionStories.stream()
                .filter(stories -> !stories.getIsQuestionModified().booleanValue())
                .map(stories -> stories.getRecQuestionNo())
                .toList();
    };

}
