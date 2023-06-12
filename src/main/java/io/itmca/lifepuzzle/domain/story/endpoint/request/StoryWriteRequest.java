package io.itmca.lifepuzzle.domain.story.endpoint.request;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoryWriteRequest {
  Long heroNo;
  Long recQuestionNo;
  Boolean recQuestionModified;
  String helpQuestionText;
  LocalDate date;
  String title;
  String storyText;

  public Story toStory(Long userNo) {
    var storyKey = generatedStoryKey();
    return Story.builder()
        .storyKey(storyKey)
        .heroNo(heroNo)
        .userNo(userNo)
        .recQuestionNo(recQuestionNo == null ? -1 : recQuestionNo)
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
