package io.itmca.lifepuzzle.domain.story.file;

import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_IMAGE_BASE_PATH_FORMAT;
import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_IMAGE_RESIZING_THUMBNAIL_WIDTH;
import static io.itmca.lifepuzzle.global.util.FileUtil.handleFileNameContents;

import io.itmca.lifepuzzle.global.file.CustomFile;
import io.itmca.lifepuzzle.global.file.Resizable;
import io.itmca.lifepuzzle.global.util.FileUtil;
import io.itmca.lifepuzzle.global.util.ImageUtil;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Slf4j
public class StoryImageFile extends CustomFile implements Resizable<StoryImageFile> {
  public StoryImageFile(Long heroId, MultipartFile file) {
    this(heroId, file, "");
  }

  public StoryImageFile(Long heroId, MultipartFile file, String postfix) {
    super(
        STORY_IMAGE_BASE_PATH_FORMAT.formatted(heroId),
        file,
        postfix
    );
  }

  public StoryImageFile(StoryImageFile storyImageFile, byte[] bytes) {
    super(storyImageFile, bytes);
  }

  @Builder
  public StoryImageFile(StoryImageFile storyImageFile, byte[] bytes, String base) {
    super(storyImageFile, bytes, base);
  }

  @Deprecated
  @Override
  public Optional<StoryImageFile> resize() {
    return resize(STORY_IMAGE_RESIZING_THUMBNAIL_WIDTH);
  }

  @Override
  public Optional<StoryImageFile> resize(int fixedWidth) {
    try {
      var imageDimension = ImageUtil.getResizeFileDimension(bytes, fixedWidth);
      var targetWidth = imageDimension.width();
      var targetHeight = imageDimension.height();

      var shouldResize = fixedWidth == targetWidth;
      var resizeImage = shouldResize
          ? ImageUtil.getResizeImage(bytes, contentType, targetWidth, targetHeight)
          : bytes;
      var resizeBasePath = base.replace("original", String.valueOf(fixedWidth));
      return Optional.of(StoryImageFile
          .builder()
          .storyImageFile(this)
          .base(resizeBasePath)
          .bytes(resizeImage)
          .build());
    } catch (Exception ignored) {
      log.error(ignored.getMessage());
      return Optional.empty();
    }
  }

  public static List<StoryImageFile> listFrom(List<MultipartFile> gallery, Long heroId) {
    return handleFileNameContents(
        gallery.stream().filter(FileUtil::isImageFile).toList(),
        (image, postfix) -> new StoryImageFile(heroId, image, postfix));
  }
}
