package io.itmca.lifepuzzle.global.file.domain;

import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_IMAGE_BASE_PATH_FORMAT;
import static io.itmca.lifepuzzle.global.util.FileUtil.handleFileNameContents;

import io.itmca.lifepuzzle.global.file.CustomFile;
import io.itmca.lifepuzzle.global.util.FileUtil;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Slf4j
public class StoryImageFile extends CustomFile {
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


  public static List<StoryImageFile> listFrom(List<MultipartFile> gallery, Long heroId) {
    return handleFileNameContents(
        gallery.stream().filter(FileUtil::isImageFile).toList(),
        (image, postfix) -> new StoryImageFile(heroId, image, postfix));
  }
}
