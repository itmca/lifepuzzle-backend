package io.itmca.lifepuzzle.domain.story.file;

import static io.itmca.lifepuzzle.global.constant.FileConstant.STORY_BASE_PATH;
import static io.itmca.lifepuzzle.global.constant.FileConstant.VIDEO_RESIZING_HEIGHT;
import static io.itmca.lifepuzzle.global.constant.FileConstant.VIDEO_RESIZING_WIDTH;

import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import io.itmca.lifepuzzle.global.infra.file.Resizable;
import java.io.File;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class StoryVideoFile extends CustomFile implements Resizable<StoryVideoFile> {

  public StoryVideoFile(Story story, MultipartFile file) {
    this(story, file, "");
  }

  public StoryVideoFile(Story story, MultipartFile file, String postfix) {
    super(
        String.join(File.separator, STORY_BASE_PATH, story.getStoryKey(), "video"),
        file,
        postfix
    );
  }

  @Builder
  private StoryVideoFile(String base, String fileName, byte[] bytes) {
    super(base, fileName, bytes);
  }

  @Override
  public StoryVideoFile resize() {
    var customRes = new IVSize();
    customRes.setWidth(VIDEO_RESIZING_WIDTH);
    customRes.setHeight(VIDEO_RESIZING_HEIGHT);

    try {
      var resizedVideo =
          new IVCompressor().reduceVideoSizeWithCustomRes(bytes, VideoFormats.MP4, customRes);

      return StoryVideoFile.builder()
          .bytes(resizedVideo)
          .base(base)
          .fileName(fileName)
          .build();
    } catch (VideoException e) {
      throw new RuntimeException(e);
    }
  }
}