package io.itmca.lifepuzzle.global.infra.file.service;

import io.itmca.lifepuzzle.global.exception.S3UploadFailException;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import io.itmca.lifepuzzle.global.infra.file.repository.S3Repository;
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
      upload(customFile);
    }
  }
}
