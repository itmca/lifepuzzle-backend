package io.itmca.lifepuzzle.global.infra.file;

import static io.itmca.lifepuzzle.global.util.FileUtil.addRandomValueFilePrefix;
import static io.itmca.lifepuzzle.global.util.FileUtil.encodingToUTF8;

import io.itmca.lifepuzzle.global.constant.FileConstant;
import java.io.IOException;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class VoiceCustomFile implements CustomFile {

  private final MultipartFile multipartFile;
  private final byte[] bytes;
  private final String basePath;
  private final String tempPath;
  private final String fileName;

  public VoiceCustomFile(String basePath, MultipartFile multipartFile) {
    this.multipartFile = multipartFile;
    this.fileName = addRandomValueFilePrefix(encodingToUTF8(multipartFile.getOriginalFilename()));
    this.basePath = basePath;
    this.tempPath = FileConstant.TEMP_FOLDER_PATH;

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