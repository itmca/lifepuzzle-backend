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
public class StoryDto {
  private String id;
  private Long heroNo;
  private String title;
  private String content;
  private String question;
  private List<String> photos;
  private List<String> videos;
  private List<String> audios;
  private List<StoryTagDto> tags;
  private List<LikeDto> likes;
  private int likeCount;
  private LocalDate date;
  private LocalDateTime createdAt;

  public static StoryDto from(Story story, Hero hero) {
    var likeDTOs = LikeDto.listFrom(story.getLikes());

    return StoryDto.builder()
        .id(story.getId())
        .heroNo(story.getHeroId())
        .title(story.getTitle())
        .content(story.getContent())
        .question(story.getUsedQuestion())
        .photos(story.getImages())
        .videos(story.getVideos())
        .audios(story.getAudios())
        .tags(List.of(StoryTagDto.from(story.getTag(hero))))
        .likes(likeDTOs)
        .likeCount(likeDTOs.size())
        .date(story.getDate())
        .createdAt(story.getCreatedAt())
        .build();
  }

}
