package io.itmca.lifepuzzle.domain.story.endpoint.request;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class StoryGalleryWriteRequest {
  private String title;
  private String content;
  private LocalDate date;
  // TODO: FE 전환 후 @HeroNo 주석 제거
  // @HeroNo
  private Long heroId;
  private List<Long> galleryIds;

  @Deprecated
  public Story toStory(Long heroId, Long userId) {
    return Story.builder()
        .id(generatedStoryKey(heroId))
        .heroId(heroId)
        .userId(userId)
        .title(title != null ? title : "")
        .content(content)
        .date(date)
        .build();
  }

  public Story toStory(Long userId) {
    return Story.builder()
        .id(generatedStoryKey(heroId))
        .heroId(heroId)
        .userId(userId)
        .title(title != null ? title : "")
        .content(content)
        .date(date)
        .build();
  }

  public String generatedStoryKey(Long heroId) {
    var now = LocalDateTime.now();
    return heroId.toString() + "-" + now.getHour() + now.getMinute() + now.getSecond();
  }
}
