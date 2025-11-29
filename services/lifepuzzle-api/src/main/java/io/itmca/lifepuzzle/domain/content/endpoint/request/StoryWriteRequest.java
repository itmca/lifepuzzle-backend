package io.itmca.lifepuzzle.domain.content.endpoint.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.itmca.lifepuzzle.domain.content.entity.Story;
import io.itmca.lifepuzzle.global.aop.HeroNo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record StoryWriteRequest(
    @HeroNo
    @JsonAlias("heroNo")
    @NotNull(message = "Hero number is required")
    Long heroId,

    @JsonAlias("recQuestionNo")
    Long questionId,
    @JsonAlias("recQuestionModified")
    Boolean questionModified,
    @JsonAlias("helpQuestionText")
    String questionText,

    @NotNull(message = "Date is required")
    LocalDate date,

    @NotBlank(message = "Title is required")
    String title,

    @JsonAlias("storyText")
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
        .title(title)
        .content(content)
        .date(date)
        .build();
  }

  public String generatedStoryKey() {
    var now = LocalDateTime.now();
    return heroId.toString() + "-" + now.getHour() + now.getMinute() + now.getSecond();
  }
}
