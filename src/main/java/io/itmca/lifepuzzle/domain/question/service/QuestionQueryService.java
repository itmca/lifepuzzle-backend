package io.itmca.lifepuzzle.domain.question.service;

import io.itmca.lifepuzzle.domain.question.entity.Question;
import io.itmca.lifepuzzle.domain.question.repository.QuestionRepository;
import io.itmca.lifepuzzle.domain.question.repository.QuestionStoryRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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

    var notUsedQuestions = questions.stream()
        .filter(question -> !alreadyUsedQuestionNumbers.contains(question.getQuestionNo()))
        .collect(Collectors.toList());

    Collections.shuffle(notUsedQuestions);

    return notUsedQuestions.stream()
        .limit(size)
        .toList();
  }

  private List<Long> findHeroUsedQuestionNumbers(Long heroNo) {
    var heroQuestionStories = questionStoryRepository.findByHeroId(heroNo);

    return heroQuestionStories.stream()
        .filter(stories -> !stories.isQuestionModified())
        .map(stories -> stories.getRecQuestionNo())
        .toList();
  }
}
