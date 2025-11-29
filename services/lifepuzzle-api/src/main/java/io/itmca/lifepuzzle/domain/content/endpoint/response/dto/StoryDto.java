package io.itmca.lifepuzzle.domain.content.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.content.entity.Story;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public record StoryDto(
    String id,
    Long heroNo,
    String title,
    String content,
    String question,
    List<String> photos,
    List<String> videos,
    List<String> audios,
    List<StoryTagDto> tags,
    List<LikeDto> likes,
    int likeCount,
    LocalDate date,
    LocalDateTime createdAt
) {

  public static StoryDto from(Story story, Hero hero) {
    var likeDTOs = LikeDto.listFrom(story.getLikes());

    return new StoryDto(
        story.getId(),
        story.getHeroId(),
        story.getTitle(),
        story.getContent(),
        story.getUsedQuestion(),
        Collections.emptyList(), // TODO: Gallery 시스템으로 이관됨
        Collections.emptyList(), // TODO: Gallery 시스템으로 이관됨
        story.getAudios(),
        List.of(StoryTagDto.from(story.getTag(hero))),
        likeDTOs,
        likeDTOs.size(),
        story.getDate(),
        story.getCreatedAt()
    );
  }
}
