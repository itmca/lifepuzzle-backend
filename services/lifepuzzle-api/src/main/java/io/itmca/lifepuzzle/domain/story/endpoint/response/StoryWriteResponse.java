package io.itmca.lifepuzzle.domain.story.endpoint.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoryWriteResponse {
  private String storyKey;
}
