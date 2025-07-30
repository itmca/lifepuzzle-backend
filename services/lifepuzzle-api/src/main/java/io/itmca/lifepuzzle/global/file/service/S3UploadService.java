package io.itmca.lifepuzzle.global.file.service;

import static java.io.File.separator;

import io.itmca.lifepuzzle.global.exception.S3UploadFailException;
import io.itmca.lifepuzzle.global.file.CustomFile;
import io.itmca.lifepuzzle.global.file.repository.S3Repository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3UploadService {
  private final S3Repository s3Repository;

  public void upload(CustomFile customFile) {
    try {
      s3Repository.upload(customFile);
    } catch (IOException e) {
      throw new S3UploadFailException(customFile.getFileName(), e);
    }
  }

  public void upload(List<? extends CustomFile> customFiles) {
    for (var customFile : customFiles) {
      if (customFile == null || customFile.isUploaded()) {
        continue;
      }
      upload(customFile);
    }
  }

  public void delete(String base) {
    try {
      s3Repository.delete(base);
    } catch (IOException e) {
      throw new S3UploadFailException(base, e);
    }
  }

  public void delete(String base, List<String> fileNames) {
    for (var fileName : fileNames) {
      delete(String.join(separator, base, fileName));
    }
  }
}
