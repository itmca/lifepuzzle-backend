package io.itmca.lifepuzzle.domain.story.endpoint.request;

import io.itmca.lifepuzzle.domain.story.type.AgeGroup;
import lombok.Getter;

@Getter
public class GalleryWriteRequest {
  private Long heroId;
  private AgeGroup ageGroup;
}
