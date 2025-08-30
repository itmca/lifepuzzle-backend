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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class S3Repository implements FileRepository {
  private final S3Operations s3Operations;
  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucket;

  public void upload(CustomFile customFile) throws IOException {
    final String fileName = customFile.getFileName();
    final String s3Key = customFile.getBase() + fileName;
    final String tempFolder = FileConstant.TEMP_FOLDER_PATH + File.separator + customFile.getBase();
    final String localFilePath = tempFolder + File.separator + fileName;
    
    log.debug("S3Repository upload - bucket: {}, s3Key: {}, localPath: {}", bucket, s3Key, localFilePath);

    // Create temp folder if not exists
    if (!FileUtil.isExistFolder(tempFolder)) {
      log.debug("Creating temp folder: {}", tempFolder);
      FileUtil.createAllFolder(tempFolder);
    }

    // Save to local temp file
    File localFile = null;
    try {
      localFile = FileUtil.saveMultiPartFileInLocal(customFile.getBytes(), localFilePath);
      log.debug("Local temp file created - path: {}, size: {} bytes", localFile.getAbsolutePath(), localFile.length());

      // Upload to S3
      try (InputStream inputFile = new FileInputStream(localFile)) {
        log.debug("Starting S3 upload to bucket: {}, key: {}", bucket, s3Key);
        s3Operations.upload(bucket, s3Key, inputFile);
        log.debug("S3 upload completed successfully - key: {}", s3Key);
      }
      
    } catch (IOException e) {
      log.error("S3 upload failed - bucket: {}, key: {}, localPath: {}, error: {}", 
          bucket, s3Key, localFilePath, e.getMessage(), e);
      throw e;
    } finally {
      // Clean up local temp file
      if (localFile != null && localFile.exists()) {
        boolean deleted = localFile.delete();
        log.debug("Local temp file cleanup - path: {}, deleted: {}", localFile.getAbsolutePath(), deleted);
      }
    }
  }

  public void delete(String base) throws IOException {
    var s3Resources = s3Operations.listObjects(bucket, base);

    for (S3Resource s3Resource : s3Resources) {
      s3Operations.deleteObject(bucket, s3Resource.getFilename());
    }
  }
}
