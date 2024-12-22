package io.itmca.lifepuzzle.domain.gallery.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.story.endpoint.response.dto.StoryDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GalleryDTO {
  private Long id;
  private int index;
  private String type;
  private String url;
  private StoryDTO story;
}
