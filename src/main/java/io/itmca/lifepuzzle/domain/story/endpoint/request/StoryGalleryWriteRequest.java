package io.itmca.lifepuzzle.domain.story.endpoint.request;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class StoryGalleryWriteRequest {
  private String title;
  private String content;
  private LocalDate date;
  private List<Long> galleryIds;

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

  public String generatedStoryKey(Long heroId) {
    var now = LocalDateTime.now();
    return heroId.toString() + "-" + now.getHour() + now.getMinute() + now.getSecond();
  }
}
