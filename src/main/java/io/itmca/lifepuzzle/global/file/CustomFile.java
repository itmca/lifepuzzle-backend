package io.itmca.lifepuzzle.global.file;

import static io.itmca.lifepuzzle.global.constants.FileConstant.FILE_DUPLICATE_PREFIX;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public abstract class CustomFile {
  protected final String base;
  protected final String fileName;
  protected final byte[] bytes;
  protected final String contentType;
  private final boolean uploaded;

  public CustomFile(String base, MultipartFile file) {
    this(base, file, "");
  }

  public CustomFile(String base, MultipartFile file, String postfix) {
    this.base = base;
    this.fileName = normalizeFileName(file.getOriginalFilename(), postfix);
    this.uploaded = file.getOriginalFilename().startsWith(FILE_DUPLICATE_PREFIX);
    this.contentType = file.getContentType();

    try {
      this.bytes = file.getBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected CustomFile(CustomFile customFile, byte[] bytes) {
    this.base = customFile.getBase();
    this.fileName = customFile.getFileName();
    this.bytes = bytes;
    this.uploaded = customFile.uploaded;
    this.contentType = customFile.getContentType();
  }

  private String normalizeFileName(String fileName, String postfix) {
    var normalized = URLDecoder.decode(fileName, StandardCharsets.UTF_8)
        .replaceFirst("^" + FILE_DUPLICATE_PREFIX, "");

    return fileName.startsWith(FILE_DUPLICATE_PREFIX) ? normalized :
        addPostfixToFileName(normalized, postfix);
  }

  private String addPostfixToFileName(String fileName, String postfix) {
    var split = fileName.split("[.]");
    split[0] = split[0] + postfix;

    return Arrays.stream(split).collect(Collectors.joining("."));
  }
}
