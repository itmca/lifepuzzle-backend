package io.itmca.lifepuzzle.domain.question.service;

import io.itmca.lifepuzzle.domain.question.entity.Question;
import io.itmca.lifepuzzle.domain.question.repository.QuestionRepository;
import io.itmca.lifepuzzle.domain.question.repository.QuestionStoryRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class QuestionQueryService {
  private final QuestionRepository questionRepository;
  private final QuestionStoryRepository questionStoryRepository;

  public List<Question> getRecommendQuestion(String category, Long heroNo, Long size) {
    var questions = !StringUtils.hasText(category)
        ? questionRepository.findAll()
        : questionRepository.findByCategory(category);

    var alreadyUsedQuestionNumbers = heroNo > 0
        ? findHeroUsedQuestionNumbers(heroNo)
        : new ArrayList<Long>();

    return questions.stream()
        .filter(question -> !alreadyUsedQuestionNumbers.contains(question.getQuestionNo()))
        .limit(size)
        .toList();
  }

  private List<Long> findHeroUsedQuestionNumbers(Long heroNo) {
    var heroQuestionStories = questionStoryRepository.findByHeroNo(heroNo);

    return heroQuestionStories.stream()
        .filter(stories -> !stories.isQuestionModified())
        .map(stories -> stories.getRecQuestionNo())
        .toList();
  }
}
