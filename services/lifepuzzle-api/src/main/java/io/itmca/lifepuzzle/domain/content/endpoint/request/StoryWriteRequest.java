package io.itmca.lifepuzzle.domain.content.endpoint.request;

import io.itmca.lifepuzzle.domain.content.entity.Story;
import io.itmca.lifepuzzle.global.aop.HeroNo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record StoryWriteRequest(
    @HeroNo
    @NotNull(message = "Hero number is required")
    Long heroId,

    Long questionId,
    Boolean questionModified,
    String questionText,

    @NotBlank(message = "Story text is required")
    String content
) {
  public Story toStory(Long userNo) {
    var storyKey = generatedStoryKey();
    return Story.builder()
        .id(storyKey)
        .heroId(heroId)
        .userId(userNo)
        .recQuestionId(questionId == null ? -1 : questionId)
        .isQuestionModified(questionModified == null ? false : questionModified)
        .usedQuestion(questionText)
        .content(content)
        .build();
  }

  public String generatedStoryKey() {
    var now = LocalDateTime.now();
    return heroId.toString() + "-" + now.getHour() + now.getMinute() + now.getSecond();
  }
}
