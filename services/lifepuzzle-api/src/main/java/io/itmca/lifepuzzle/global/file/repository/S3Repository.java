package io.itmca.lifepuzzle.global.file.repository;

import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import io.itmca.lifepuzzle.global.constants.FileConstant;
import io.itmca.lifepuzzle.global.file.CustomFile;
import io.itmca.lifepuzzle.global.util.FileUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class S3Repository implements FileRepository {
  private final S3Operations s3Operations;
  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucket;

  public void upload(CustomFile customFile) throws IOException {
    var tempFolder = FileConstant.TEMP_FOLDER_PATH + File.separator + customFile.getBase();

    if (!FileUtil.isExistFolder(tempFolder)) {
      FileUtil.createAllFolder(tempFolder);
    }

    var localFile = FileUtil.saveMultiPartFileInLocal(customFile.getBytes(),
        tempFolder + File.separator + customFile.getFileName());

    try (InputStream inputFile = new FileInputStream(localFile)) {
      s3Operations.upload(bucket, customFile.getBase() + customFile.getFileName(), inputFile);
    }

    localFile.delete();
  }

  public void delete(String base) throws IOException {
    var s3Resources = s3Operations.listObjects(bucket, base);

    for (S3Resource s3Resource : s3Resources) {
      s3Operations.deleteObject(bucket, s3Resource.getFilename());
    }
  }
}
