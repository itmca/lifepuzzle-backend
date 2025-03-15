package io.itmca.lifepuzzle.domain.story.endpoint.response;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.story.endpoint.response.dto.StoryDto;
import io.itmca.lifepuzzle.domain.story.endpoint.response.dto.StoryTagDto;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.domain.story.type.AgeGroup;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoryQueryResponse {

  private List<StoryDto> stories;
  private List<StoryTagDto> tags;

  /* [Debugging]
      hero가 -1인 경우 빈 데이터를 넘겨줘야 함.
   */
  public static StoryQueryResponse getEmptyResponse() {
    return StoryQueryResponse.builder()
        .stories(new ArrayList<>())
        .tags(new ArrayList<>())
        .build();
  }

  public static StoryQueryResponse from(List<Story> stories, Hero hero, List<AgeGroup> ageGroups) {
    var storyDTOs = stories.stream().map(story -> StoryDto.from(story, hero)).toList();
    var storyTags = ageGroups.stream()
        .map(ageGroup -> StoryTagDto.builder()
            .key(ageGroup.getRepresentativeAge().toString())
            .displayName(ageGroup.getDisplayName())
            .priority(ageGroup.getRepresentativeAge())
            .build())
        .sorted()
        .toList();

    return StoryQueryResponse.builder()
        .stories(storyDTOs)
        .tags(storyTags)
        .build();
  }

}
