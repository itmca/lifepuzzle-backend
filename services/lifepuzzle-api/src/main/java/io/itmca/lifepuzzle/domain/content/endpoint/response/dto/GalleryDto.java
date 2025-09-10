package io.itmca.lifepuzzle.domain.content.endpoint.response.dto;

import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_IMAGE_RESIZING_GENERAL_WIDTH;
import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_IMAGE_RESIZING_PINCH_ZOOM_WIDTH;
import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_IMAGE_RESIZING_THUMBNAIL_WIDTH;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.itmca.lifepuzzle.domain.content.entity.Gallery;
import io.itmca.lifepuzzle.domain.content.entity.StoryGallery;
import io.itmca.lifepuzzle.domain.content.type.GalleryType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GalleryDto(
    Long id,
    int index,
    GalleryType type,
    @Deprecated
    String url,
    String thumbnailUrl,
    String bigSizeUrl,
    // 향후 N:M 관계를 고려하여 DB 테이블 설계되어 있지만 현재 정책은 1:N 관계이므로 단건만 응답
    StoryGalleryDto story
) {
  public static GalleryDto from(Gallery gallery, int index) {
    var storyDTO = gallery.getStoryMaps().stream()
        .map(StoryGallery::getStory)
        .map(StoryGalleryDto::from)
        .findFirst()
        .orElse(null);

    return new GalleryDto(
        gallery.getId(),
        index,
        gallery.getGalleryType(),
        gallery.getImageUrl(STORY_IMAGE_RESIZING_GENERAL_WIDTH),
        gallery.getImageUrl(STORY_IMAGE_RESIZING_THUMBNAIL_WIDTH),
        gallery.getImageUrl(STORY_IMAGE_RESIZING_PINCH_ZOOM_WIDTH),
        storyDTO
    );
  }
}
