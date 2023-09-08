package io.itmca.lifepuzzle.global.infra.file;

import static io.itmca.lifepuzzle.global.constant.FileConstant.FILE_DUPLICATE_PREFIX;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public abstract class CustomFile {
  protected final String base;
  protected final String fileName;
  protected final byte[] bytes;

  public CustomFile(String base, MultipartFile file) {
    this.base = base;
    this.fileName = normalizeFileName(file.getOriginalFilename());
    try {
      this.bytes = file.getBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected CustomFile(String base, String fileName, byte[] bytes) {
    this.base = base;
    this.fileName = fileName;
    this.bytes = bytes;
  }

  private String normalizeFileName(String fileName) {
    return URLDecoder.decode(fileName, StandardCharsets.UTF_8);
  }

  public Boolean isUploaded() {
    return fileName.startsWith(FILE_DUPLICATE_PREFIX);
  }
}