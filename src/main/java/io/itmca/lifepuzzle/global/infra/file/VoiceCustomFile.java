package io.itmca.lifepuzzle.global.infra.file;

import static io.itmca.lifepuzzle.global.util.FileUtil.addRandomValueFilePrefix;

import java.io.IOException;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class VoiceCustomFile implements CustomFile {

  private final MultipartFile multipartFile;
  private final byte[] bytes;
  private final String base;
  private final String fileName;

  public VoiceCustomFile(MultipartFile multipartFile) {
    this.multipartFile = multipartFile;
    this.fileName = addRandomValueFilePrefix(multipartFile);
    this.base = "voice";

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