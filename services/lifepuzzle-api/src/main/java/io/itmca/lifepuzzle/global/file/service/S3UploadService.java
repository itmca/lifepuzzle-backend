package io.itmca.lifepuzzle.global.file.service;

import static java.io.File.separator;

import io.itmca.lifepuzzle.global.exception.S3UploadFailException;
import io.itmca.lifepuzzle.global.file.CustomFile;
import io.itmca.lifepuzzle.global.file.repository.S3Repository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService {
  private final S3Repository s3Repository;

  public void upload(CustomFile customFile) {
    final long startTime = System.currentTimeMillis();
    
    log.info("Starting S3 upload - fileName: {}, base: {}, fileSize: {} bytes", 
        customFile.getFileName(), customFile.getBase(), customFile.getBytes().length);
    
    try {
      s3Repository.upload(customFile);
      final long duration = System.currentTimeMillis() - startTime;
      
      log.info("S3 upload successful - fileName: {}, duration: {}ms", 
          customFile.getFileName(), duration);
    } catch (IOException e) {
      final long duration = System.currentTimeMillis() - startTime;
      
      log.error("S3 upload failed - fileName: {}, base: {}, fileSize: {} bytes, duration: {}ms, error: {}", 
          customFile.getFileName(), customFile.getBase(), customFile.getBytes().length, 
          duration, e.getMessage(), e);
      
      throw new S3UploadFailException(customFile.getFileName(), e);
    }
  }

  public void upload(String key, byte[] bytes) {
    final long startTime = System.currentTimeMillis();

    log.info("Starting S3 upload - key: {}, fileSize: {} bytes", key, bytes.length);

    try {
      s3Repository.upload(key, bytes);
      final long duration = System.currentTimeMillis() - startTime;

      log.info("S3 upload successful - key: {}, duration: {}ms", key, duration);
    } catch (IOException e) {
      final long duration = System.currentTimeMillis() - startTime;

      log.error("S3 upload failed - key: {}, fileSize: {} bytes, duration: {}ms, error: {}",
          key, bytes.length, duration, e.getMessage(), e);

      throw new S3UploadFailException(key, e);
    }
  }

  public void upload(List<? extends CustomFile> customFiles) {
    log.info("Starting batch S3 upload - total files: {}", customFiles.size());
    
    int uploadedCount = 0;
    int skippedCount = 0;
    
    for (var customFile : customFiles) {
      if (customFile == null || customFile.isUploaded()) {
        skippedCount++;
        log.debug("Skipping file - customFile is null: {}, already uploaded: {}", 
            customFile == null, customFile != null && customFile.isUploaded());
        continue;
      }
      
      try {
        upload(customFile);
        uploadedCount++;
      } catch (S3UploadFailException e) {
        log.error("Failed to upload file in batch - fileName: {}, continuing with next file", 
            customFile.getFileName());
        throw e; // Re-throw to maintain existing behavior
      }
    }
    
    log.info("Batch S3 upload completed - uploaded: {}, skipped: {}, total: {}", 
        uploadedCount, skippedCount, customFiles.size());
  }

  public void delete(String base) {
    log.info("Starting S3 delete - base path: {}", base);
    
    try {
      s3Repository.delete(base);
      log.info("S3 delete successful - base path: {}", base);
    } catch (IOException e) {
      log.error("S3 delete failed - base path: {}, error: {}", base, e.getMessage(), e);
      throw new S3UploadFailException(base, e);
    }
  }

  public void delete(String base, List<String> fileNames) {
    for (var fileName : fileNames) {
      delete(String.join(separator, base, fileName));
    }
  }
}
