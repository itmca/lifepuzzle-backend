package io.itmca.lifepuzzle.global.infra.file;

import static io.itmca.lifepuzzle.global.util.FileUtil.addRandomValueFilePrefix;

import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;
import java.io.IOException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@RequiredArgsConstructor
public class VideoCustomFile implements CustomFile {
  private final byte[] bytes;
  private final String base;
  private final String fileName;

  public VideoCustomFile(MultipartFile multipartFile) {
    this.fileName = addRandomValueFilePrefix(multipartFile);
    this.base = "video";

    try {
      this.bytes = multipartFile.getBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public CustomFile resize() {
    var customRes = new IVSize();
    customRes.setWidth(854);
    customRes.setHeight(480);

    try {
      var resizedVideo =
          new IVCompressor().reduceVideoSizeWithCustomRes(bytes, VideoFormats.MP4, customRes);
      
      return VideoCustomFile.builder()
          .bytes(resizedVideo)
          .base(base)
          .fileName(fileName)
          .build();
    } catch (VideoException e) {
      throw new RuntimeException(e);
    }
  }
}