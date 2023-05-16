package io.itmca.lifepuzzle.domain.story.endpoint.response;

import io.itmca.lifepuzzle.domain.story.AgeGroup;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoryTagResponse implements Comparable<StoryTagResponse> {
  private String key;
  private String displayName;
  private Long priority;

  public static StoryTagResponse from(AgeGroup ageGroup) {
    return StoryTagResponse.builder()
        .key(ageGroup.getPriority().toString())
        .displayName(ageGroup.getDisplayName())
        .priority(ageGroup.getPriority())
        .build();
  }

  @Override
  public int compareTo(StoryTagResponse o) {
    return Long.compare(this.priority, o.priority);
  }
}
