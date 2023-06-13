package io.itmca.lifepuzzle.global.infra.file;

import static io.itmca.lifepuzzle.global.util.FileUtil.addRandomValueFilePrefix;

import java.io.IOException;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ImageCustomFile implements CustomFile {
  private final String base;
  private final String fileName;
  private final byte[] bytes;

  public ImageCustomFile(MultipartFile multipartFile) {
    this.fileName = addRandomValueFilePrefix(multipartFile);
    this.base = "image";

    try {
      this.bytes = multipartFile.getBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public CustomFile resize() {
    return null;
  }
}