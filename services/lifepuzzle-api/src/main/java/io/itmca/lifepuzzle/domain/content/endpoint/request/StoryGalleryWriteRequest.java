package io.itmca.lifepuzzle.domain.content.endpoint.request;

import io.itmca.lifepuzzle.domain.content.entity.Story;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

/**
 * Request DTO for creating a story with gallery images.
 *
 * @param heroId hero ID
 * @param title story title
 * @param content story content  
 * @param date story date
 * @param galleryIds associated gallery IDs
 */
public record StoryGalleryWriteRequest(String title, String content, LocalDate date, Long heroId,
                                       List<Long> galleryIds) {
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
