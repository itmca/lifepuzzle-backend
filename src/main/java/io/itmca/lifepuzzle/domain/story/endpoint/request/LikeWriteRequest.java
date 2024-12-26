package io.itmca.lifepuzzle.domain.story.endpoint.request;

import io.itmca.lifepuzzle.domain.story.type.LikeType;
import lombok.Getter;

@Getter
public class LikeWriteRequest {
  private String targetId;
  private LikeType type;
}
