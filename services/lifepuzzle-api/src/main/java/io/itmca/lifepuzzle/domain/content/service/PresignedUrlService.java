package io.itmca.lifepuzzle.domain.content.service;

import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_IMAGE_BASE_PATH_FORMAT;

import io.itmca.lifepuzzle.domain.content.endpoint.response.PresignedUrlResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class PresignedUrlService {

  private final S3Presigner s3Presigner;

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucket;

  public List<PresignedUrlResponse> createPresignedUrls(Long heroId, int count) {
    List<PresignedUrlResponse> urls = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      var path = STORY_IMAGE_BASE_PATH_FORMAT.formatted(heroId);
      var key = path + UUID.randomUUID() + ".jpg";

      var putObjectRequest = PutObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .contentType("image/jpeg")
          .cacheControl("max-age=31536000")
          .build();

      var presignRequest = PutObjectPresignRequest.builder()
          .signatureDuration(Duration.ofMinutes(10))
          .putObjectRequest(putObjectRequest)
          .build();

      var presignedUrl = s3Presigner.presignPutObject(presignRequest).url();

      urls.add(new PresignedUrlResponse(key, presignedUrl.toString()));
    }

    return urls;
  }
}
