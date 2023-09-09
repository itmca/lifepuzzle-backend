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
  private final boolean uploaded;

  public CustomFile(String base, MultipartFile file) {
    this(base, file, "");
  }

  public CustomFile(String base, MultipartFile file, String postfix) {
    this.base = base;
    this.fileName = normalizeFileName(file.getOriginalFilename(), postfix);
    this.uploaded = fileName.startsWith(FILE_DUPLICATE_PREFIX);

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
    this.uploaded = fileName.startsWith(FILE_DUPLICATE_PREFIX);
  }

  private String normalizeFileName(String fileName, String postfix) {
    var normalized = URLDecoder.decode(fileName, StandardCharsets.UTF_8)
        .replaceFirst("^" + FILE_DUPLICATE_PREFIX, "");

    return fileName.startsWith(FILE_DUPLICATE_PREFIX) ? normalized : normalized + postfix;
  }
}
