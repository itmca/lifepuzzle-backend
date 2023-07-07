package io.itmca.lifepuzzle.domain.story.file;

import static io.itmca.lifepuzzle.global.constant.FileConstant.STORY_BASE_PATH;
import static io.itmca.lifepuzzle.global.constant.FileConstant.VIDEO_RESIZING_HEIGHT;
import static io.itmca.lifepuzzle.global.constant.FileConstant.VIDEO_RESIZING_WIDTH;
import static io.itmca.lifepuzzle.global.util.FileUtil.addRandomValueFilePrefix;

import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;
import io.itmca.lifepuzzle.domain.story.entity.Story;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import java.io.File;
import java.io.IOException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@RequiredArgsConstructor
public class StoryVideoFile implements CustomFile {
  private final byte[] bytes;
  private final String base;
  private final String fileName;
  private final String originalFileName;

  public StoryVideoFile(Story story, MultipartFile multipartFile) {
    this.fileName = addRandomValueFilePrefix(multipartFile);
    this.originalFileName = multipartFile.getOriginalFilename();
    this.base = String.join(File.separator, STORY_BASE_PATH, story.getStoryKey(), "video");

    try {
      this.bytes = multipartFile.getBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public CustomFile resize() {
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
          .originalFileName(originalFileName)
          .build();
    } catch (VideoException e) {
      throw new RuntimeException(e);
    }
  }
}