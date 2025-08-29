package io.itmca.lifepuzzle.domain.content.endpoint.request;

import io.itmca.lifepuzzle.domain.content.type.AgeGroup;
import lombok.Getter;

@Getter
public class GalleryWriteRequest {
  private Long heroId;
  private AgeGroup ageGroup;
}
