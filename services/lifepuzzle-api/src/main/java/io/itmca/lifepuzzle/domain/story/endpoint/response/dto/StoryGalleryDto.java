package io.itmca.lifepuzzle.domain.story.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.story.entity.Story;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoryGalleryDto {
  private String id;
  private String title;
  private String content;
  private List<String> audios;
  private LocalDate date;

  public static StoryGalleryDto from(Story story) {
    return StoryGalleryDto.builder()
        .id(story.getId())
        .title(story.getTitle())
        .content(story.getContent())
        .audios(story.getAudios())
        .date(story.getDate())
        .build();
  }
}
