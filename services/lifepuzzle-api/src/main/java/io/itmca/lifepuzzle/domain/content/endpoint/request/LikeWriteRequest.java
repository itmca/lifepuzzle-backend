package io.itmca.lifepuzzle.domain.content.endpoint.request;

import io.itmca.lifepuzzle.domain.content.type.LikeType;
import lombok.Getter;

@Getter
public class LikeWriteRequest {
  private String targetId;
  private LikeType type;
}
