package io.itmca.lifepuzzle.domain.story.endpoint.response.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.itmca.lifepuzzle.domain.story.entity.StoryPhoto;
import io.itmca.lifepuzzle.domain.story.entity.StoryPhotoMap;
import io.itmca.lifepuzzle.domain.story.type.GalleryType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GalleryDto {
  private Long id;
  private int index;
  private GalleryType type;
  private String url;
  // 향후 N:M 관계를 고려하여 DB 테이블 설계되어 있지만 현재 정책은 1:N 관계이므로 단건만 응답
  private GalleryStoryDto story;

  public static GalleryDto from(StoryPhoto photo, int index) {
    var storyDTO = photo.getStoryMaps().stream()
        .map(StoryPhotoMap::getStory)
        .map(GalleryStoryDto::from)
        .findFirst()
        .orElse(null);

    return GalleryDto.builder()
        .id(photo.getId())
        .index(index)
        .type(photo.getGalleryType())
        .url(photo.getGalleryUrl())
        .story(storyDTO)
        .build();
  }
}
