package io.itmca.aivideogenerator.global.service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService {
  
  private final S3Client s3Client;
  
  @Value("${aws.s3.bucket}")
  private String bucketName;
  
  @Value("${aws.s3.region}")
  private String region;
  
  public String uploadVideoFile(File videoFile, Long heroId, Long galleryId, Long drivingVideoId) {
    try {
      String fileName = generateVideoFileName(heroId, galleryId, drivingVideoId);
      String s3Key = "ai-videos/" + fileName;
      
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(s3Key)
          .contentType("video/mp4")
          .build();
      
      s3Client.putObject(putObjectRequest, RequestBody.fromFile(videoFile));
      
      String s3Url = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, s3Key);
      
      log.info("Successfully uploaded video file to S3: {}", s3Url);
      return s3Url;
      
    } catch (Exception e) {
      log.error("Failed to upload video file to S3", e);
      throw new RuntimeException("S3 upload failed", e);
    }
  }
  
  private String generateVideoFileName(Long heroId, Long galleryId, Long drivingVideoId) {
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    return String.format("hero_%d_gallery_%d_driving_%d_%s.mp4", 
        heroId, galleryId, drivingVideoId, timestamp);
  }
}