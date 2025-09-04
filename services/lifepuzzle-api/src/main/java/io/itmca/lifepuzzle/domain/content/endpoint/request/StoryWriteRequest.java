package io.itmca.lifepuzzle.domain.content.endpoint.request;

import io.itmca.lifepuzzle.domain.content.entity.Story;
import io.itmca.lifepuzzle.global.aop.HeroNo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record StoryWriteRequest(
    @HeroNo
    @NotNull(message = "Hero number is required")
    Long heroNo,
    
    Long recQuestionNo,
    Boolean recQuestionModified,
    String helpQuestionText,
    
    @NotNull(message = "Date is required")
    LocalDate date,
    
    @NotBlank(message = "Title is required")
    String title,
    
    @NotBlank(message = "Story text is required")
    String storyText
) {
  public Story toStory(Long userNo) {
    var storyKey = generatedStoryKey();
    return Story.builder()
        .id(storyKey)
        .heroId(heroNo)
        .userId(userNo)
        .recQuestionId(recQuestionNo == null ? -1 : recQuestionNo)
        .isQuestionModified(recQuestionModified == null ? false : recQuestionModified)
        .usedQuestion(helpQuestionText)
        .title(title)
        .content(storyText)
        .date(date)
        .build();
  }

  public String generatedStoryKey() {
    var now = LocalDateTime.now();
    return heroNo.toString() + "-" + now.getHour() + now.getMinute() + now.getSecond();
  }
}
