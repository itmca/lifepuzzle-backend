package io.itmca.lifepuzzle.domain.content.endpoint.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoryWriteResponse {
  private String storyKey;
}
