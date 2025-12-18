package io.itmca.lifepuzzle.domain.content.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.content.entity.Story;
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
  private String content;
  private List<String> audios;

  public static StoryGalleryDto from(Story story) {
    return StoryGalleryDto.builder()
        .id(story.getId())
        .content(story.getContent())
        .audios(story.getAudios())
        .build();
  }
}
