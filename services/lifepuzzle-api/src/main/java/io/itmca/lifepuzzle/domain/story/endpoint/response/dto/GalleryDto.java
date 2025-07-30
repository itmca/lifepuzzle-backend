package io.itmca.lifepuzzle.domain.story.endpoint.response.dto;

import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_IMAGE_RESIZING_LIST_WIDTH;
import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_IMAGE_RESIZING_PINCH_ZOOM_WIDTH;
import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_IMAGE_RESIZING_THUMBNAIL_WIDTH;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.itmca.lifepuzzle.domain.story.entity.Gallery;
import io.itmca.lifepuzzle.domain.story.entity.StoryGallery;
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
  @Deprecated
  private String url;
  private String thumbnailUrl;
  private String bigSizeUrl;
  // 향후 N:M 관계를 고려하여 DB 테이블 설계되어 있지만 현재 정책은 1:N 관계이므로 단건만 응답
  private StoryGalleryDto story;

  public static GalleryDto from(Gallery gallery, int index) {
    var storyDTO = gallery.getStoryMaps().stream()
        .map(StoryGallery::getStory)
        .map(StoryGalleryDto::from)
        .findFirst()
        .orElse(null);

    return GalleryDto.builder()
        .id(gallery.getId())
        .index(index)
        .type(gallery.getGalleryType())
        .url(gallery.getImageUrl(STORY_IMAGE_RESIZING_LIST_WIDTH))
        .bigSizeUrl(gallery.getImageUrl(STORY_IMAGE_RESIZING_PINCH_ZOOM_WIDTH))
        .thumbnailUrl(gallery.getImageUrl(STORY_IMAGE_RESIZING_THUMBNAIL_WIDTH))
        .story(storyDTO)
        .build();
  }
}
