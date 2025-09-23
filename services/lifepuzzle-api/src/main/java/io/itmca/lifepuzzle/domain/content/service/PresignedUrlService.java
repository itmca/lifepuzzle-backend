package io.itmca.lifepuzzle.domain.content.service;

import static io.itmca.lifepuzzle.global.constants.FileConstant.NEW_STORY_IMAGE_BASE_PATH_FORMAT;

import io.itmca.lifepuzzle.domain.content.endpoint.request.PresignedUrlRequest;
import io.itmca.lifepuzzle.domain.content.endpoint.response.PresignedUrlResponse;
import io.itmca.lifepuzzle.domain.content.endpoint.response.dto.PresignedUrlDto;
import io.itmca.lifepuzzle.domain.content.entity.Gallery;
import io.itmca.lifepuzzle.domain.content.repository.GalleryRepository;
import io.itmca.lifepuzzle.domain.content.type.GalleryStatus;
import io.itmca.lifepuzzle.domain.content.type.GalleryType;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

  @Transactional
  public PresignedUrlResponse createPresignedUrls(PresignedUrlRequest request) {
    List<PresignedUrlDto> urls = new ArrayList<>();

    for (var file : request.files()) {
      var gallery = galleryRepository.save(
          Gallery.builder()
              .heroId(request.heroId())
              .url("")
              .ageGroup(request.ageGroup())
              .galleryType(GalleryType.IMAGE)
              .galleryStatus(GalleryStatus.PENDING)
              .build()
      );

      String key = buildS3Key(request.heroId(), gallery.getId(), file.fileName());
      String presignedUrl = generatePresignedUrl(key, file.contentType());

      gallery.setUrl(key);

      urls.add(new PresignedUrlDto(key, presignedUrl));
    }

    return new PresignedUrlResponse(urls);
  }

  private String buildS3Key(Long heroId, Long galleryId, String fileName) {
    return NEW_STORY_IMAGE_BASE_PATH_FORMAT.formatted(heroId, galleryId) + fileName;
  }

  private String generatePresignedUrl(String key, String contentType) {
    var putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType(contentType)
        .cacheControl("public, max-age=31536000, immutable")
        .build();

    var presignRequest = PutObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10))
        .putObjectRequest(putObjectRequest)
        .build();

    return s3Presigner.presignPutObject(presignRequest).url().toString();
  }

}
