package io.itmca.lifepuzzle.domain.question.service;

import io.itmca.lifepuzzle.domain.question.entity.Question;
import io.itmca.lifepuzzle.domain.question.repository.QuestionRepository;
import io.itmca.lifepuzzle.domain.question.repository.QuestionStoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class QuestionQueryService {
  private final QuestionRepository questionRepository;
  private final QuestionStoryRepository questionStoryRepository;

  public List<Question> getRecommendedQuestion(String category, Long heroNo, Long size) {
    var questions = !StringUtils.hasText(category)
        ? questionRepository.findAll()
        : questionRepository.findByCategory(category);
    var alreadyUsedQuestions = getUsedQuestionsOfHero(heroNo);

    return questions.stream()
        .filter(question -> !isUsedBefore(alreadyUsedQuestions, question))
        .limit(size)
        .toList();
  }

  ;

  private List<Long> getUsedQuestionsOfHero(Long heroNo) {
    var heroQuestionStories = questionStoryRepository.findByHeroNo(heroNo);

    return heroQuestionStories.stream()
        .filter(stories -> !stories.isQuestionModified())
        .map(stories -> stories.getRecQuestionNo())
        .toList();
  }

  ;

  private boolean isUsedBefore(List<Long> alreadyUsedQuestions, Question question) {
    return alreadyUsedQuestions.contains(question.getQuestionNo());
  }

}
