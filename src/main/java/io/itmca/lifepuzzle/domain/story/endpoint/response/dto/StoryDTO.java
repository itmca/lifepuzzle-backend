package io.itmca.lifepuzzle.domain.story.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class StoryDTO {
  String id;
  Long heroNo;
  String title;
  String content;
  String question;
  List<String> photos;
  List<String> videos;
  List<String> audios;
  List<StoryTagDTO> tags;
  LocalDate date;
  LocalDateTime createdAt;

  public static StoryDTO from(Story story, Hero hero) {
    return StoryDTO.builder()
        .id(story.getId())
        .heroNo(story.getHeroId())
        .title(story.getTitle())
        .content(story.getContent())
        .question(story.getUsedQuestion())
        .photos(story.getImages())
        .videos(story.getVideos())
        .audios(story.getAudios())
        .tags(List.of(StoryTagDTO.from(story.getTag(hero))))
        .date(story.getDate())
        .createdAt(story.getCreatedAt())
        .build();
  }

}
