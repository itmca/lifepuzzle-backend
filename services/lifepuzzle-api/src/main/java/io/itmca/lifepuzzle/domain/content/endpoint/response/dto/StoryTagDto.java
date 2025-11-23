package io.itmca.lifepuzzle.domain.content.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.content.type.AgeGroup;

public record StoryTagDto(
    String key,
    String displayName,
    Integer priority
) implements Comparable<StoryTagDto> {

  public static StoryTagDto from(AgeGroup ageGroup) {
    return new StoryTagDto(
        ageGroup.getRepresentativeAge().toString(),
        ageGroup.getDisplayName(),
        ageGroup.getRepresentativeAge()
    );
  }

  @Override
  public int compareTo(StoryTagDto o) {
    return Long.compare(this.priority, o.priority);
  }
}
