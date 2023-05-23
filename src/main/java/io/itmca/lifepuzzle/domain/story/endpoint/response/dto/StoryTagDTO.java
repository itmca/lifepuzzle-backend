package io.itmca.lifepuzzle.domain.story.endpoint.response.dto;

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
public class StoryTagDTO implements Comparable<StoryTagDTO> {
  private String key;
  private String displayName;
  private Integer priority;

  public static StoryTagDTO from(AgeGroup ageGroup) {
    return StoryTagDTO.builder()
        .key(ageGroup.getRepresentativeAge().toString())
        .displayName(ageGroup.getDisplayName())
        .priority(ageGroup.getRepresentativeAge())
        .build();
  }

  @Override
  public int compareTo(StoryTagDTO o) {
    return Long.compare(this.priority, o.priority);
  }
}
