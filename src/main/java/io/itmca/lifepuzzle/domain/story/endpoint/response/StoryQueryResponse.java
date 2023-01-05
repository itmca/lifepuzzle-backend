package io.itmca.lifepuzzle.domain.story.endpoint.response;

import io.itmca.lifepuzzle.domain.story.AgeGroup;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoryQueryResponse {

    private List<StoryDTO> stories;
    private List<StoryTagResponse> storyTagResponses;

    public static StoryQueryResponse from(List<Story> stories, List<AgeGroup> ageGroups){
        var storyDTOs = stories.stream().map(story -> StoryDTO.from(story)).toList();
        var storyTags = ageGroups.stream()
                .map(ageGroup -> StoryTagResponse.builder()
                        .key(ageGroup.getPriority().toString())
                        .displayName(ageGroup.getDisplayName())
                        .priority(ageGroup.getPriority())
                        .build())
                .sorted()
                .toList();

        return StoryQueryResponse.builder()
                .stories(storyDTOs)
                .storyTagResponses(storyTags)
                .build();
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    static class StoryDTO {
        String id;
        Long heroNo;
        String title;
        String content;
        List<String> photos;
        List<String> audios;
        String tags;
        LocalDate date;
        LocalDate createdAt;

        public static StoryDTO from(Story story){
            return StoryDTO.builder()
                    .id(story.getStoryKey())
                    .heroNo(story.getHeroNo())
                    .title(story.getTitle())
                    .content(story.getContent())
                    .photos(story.getImages())
                    .audios(story.getAudios())
                    .tags(story.getHashtag())
                    .date(story.getDate())
                    .createdAt(story.getCreatedAt())
                    .build();
        }

    }
}
