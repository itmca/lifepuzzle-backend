package io.itmca.lifepuzzle.domain.story.endpoint.response.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GalleryStoryDTO {
  private Long id;
  private String title;
  private String content;
  private String audio;
  private String date;
}
