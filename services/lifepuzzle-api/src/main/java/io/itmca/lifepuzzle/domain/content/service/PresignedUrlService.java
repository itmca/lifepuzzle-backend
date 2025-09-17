package io.itmca.lifepuzzle.domain.content.service;

import static io.itmca.lifepuzzle.domain.content.type.GalleryStatus.PENDING;
import static io.itmca.lifepuzzle.global.constants.FileConstant.STORY_IMAGE_BASE_PATH_FORMAT;

import io.itmca.lifepuzzle.domain.content.endpoint.request.PresignedUrlRequest;
import io.itmca.lifepuzzle.domain.content.endpoint.response.PresignedUrlResponse;
import io.itmca.lifepuzzle.domain.content.endpoint.response.dto.PresignedUrlDto;
import io.itmca.lifepuzzle.domain.content.entity.Gallery;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import io.itmca.lifepuzzle.domain.content.repository.GalleryRepository;
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
  private final GalleryRepository galleryRepository;

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucket;

  public PresignedUrlResponse createPresignedUrls(PresignedUrlRequest presignedUrlRequest) {
    List<PresignedUrlDto> urls = new ArrayList<>();

    for (var fileDto : presignedUrlRequest.files()) {
      var path = STORY_IMAGE_BASE_PATH_FORMAT.formatted(presignedUrlRequest.heroId());
      var key = path + UUID.randomUUID() + ".jpg";

      var putObjectRequest = PutObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .contentType(fileDto.contentType())
          .cacheControl("max-age=31536000")
          .build();

      var presignRequest = PutObjectPresignRequest.builder()
          .signatureDuration(Duration.ofMinutes(10))
          .putObjectRequest(putObjectRequest)
          .build();

      var presignedUrl = s3Presigner.presignPutObject(presignRequest).url();

      urls.add(new PresignedUrlDto(key, presignedUrl.toString()));

//      var gallery = Gallery.builder()
//          .heroId(presignedUrlRequest.heroId())
//          .url(String.valueOf(presignedUrl))
//          .ageGroup(presignedUrlRequest.ageGroup())
//          .galleryStatus(PENDING)
//          .build();
//
//      galleryRepository.save(gallery);
    }


    return new PresignedUrlResponse(urls);
  }
}
