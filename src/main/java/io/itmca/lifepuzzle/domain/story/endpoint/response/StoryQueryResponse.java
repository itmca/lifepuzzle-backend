package io.itmca.lifepuzzle.domain.story.endpoint.response;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.story.AgeGroup;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoryQueryResponse {

    private List<StoryDTO> stories;
    private List<StoryTagResponse> tags;

    /* [Debugging]
        hero가 -1인 경우 빈 데이터를 넘겨줘야 함.
     */
    public static StoryQueryResponse getEmptyResponse(){
        return StoryQueryResponse.builder()
                .stories(new ArrayList<>())
                .tags(new ArrayList<>())
                .build();
    }

    public static StoryQueryResponse from(List<Story> stories, Hero hero, List<AgeGroup> ageGroups){
        var storyDTOs = stories.stream().map(story -> StoryDTO.from(story, hero)).toList();
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
                .tags(storyTags)
                .build();
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    private static class StoryDTO {
        String id;
        Long heroNo;
        String title;
        String content;
        List<String> photos;
        List<String> audios;
        String hashTags;
        StoryTagResponse ageGroup;
        @Deprecated StoryTagResponse tags;
        LocalDate date;
        LocalDateTime createdAt;

        public static StoryDTO from(Story story, Hero hero){
            return StoryDTO.builder()
                    .id(story.getStoryKey())
                    .heroNo(story.getHeroNo())
                    .title(story.getTitle())
                    .content(story.getContent())
                    .photos(story.getImages())
                    .audios(story.getAudios())
                    .hashTags(story.getHashtag())
                    .ageGroup(StoryTagResponse.from(story.getTag(hero)))
                    .tags(StoryTagResponse.from(story.getTag(hero)))
                    .date(story.getDate())
                    .createdAt(story.getCreatedAt())
                    .build();
        }

    }
}
