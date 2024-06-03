package io.itmca.lifepuzzle.domain.story.file;

import static io.itmca.lifepuzzle.global.constant.FileConstant.STORY_IMAGE_BASE_PATH_FORMAT;
import static io.itmca.lifepuzzle.global.constant.FileConstant.STORY_IMAGE_RESIZING_HEIGHT;
import static io.itmca.lifepuzzle.global.constant.FileConstant.STORY_IMAGE_RESIZING_WIDTH;

import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.ImageFormats;
import io.github.techgnious.exception.ImageException;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import java.io.File;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Slf4j
public class StoryImageFile extends CustomFile {
  public StoryImageFile(Story story, MultipartFile file) {
    this(story, file, "");
  }

  public StoryImageFile(Story story, MultipartFile file, String postfix) {
    super(
        STORY_IMAGE_BASE_PATH_FORMAT.formatted(story.getStoryKey()),
        file,
        postfix
    );
  }

  @Builder
  public StoryImageFile(StoryImageFile storyImageFile, byte[] bytes) {
    super(storyImageFile, bytes);
  }


  public StoryImageFile resize() {

    var customRes = new IVSize();
    customRes.setWidth(STORY_IMAGE_RESIZING_WIDTH);
    customRes.setHeight(STORY_IMAGE_RESIZING_HEIGHT);

    try {
      var resizeImg = new IVCompressor()
                          .resizeImageWithCustomRes(bytes, ImageFormats.JPEG, customRes);

      return StoryImageFile
            .builder()
            .storyImageFile(this)
            .bytes(resizeImg)
            .build();

    } catch (ImageException e) {
      throw new RuntimeException(e);
    }
  }
}
