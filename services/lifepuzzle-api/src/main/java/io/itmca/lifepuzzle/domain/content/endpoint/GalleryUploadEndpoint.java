package io.itmca.lifepuzzle.domain.content.endpoint;

import io.itmca.lifepuzzle.domain.content.endpoint.request.PresignedUrlRequest;
import io.itmca.lifepuzzle.domain.content.endpoint.response.PresignedUrlResponse;
import io.itmca.lifepuzzle.domain.content.service.PresignedUrlService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Presigned URL")
public class GalleryUploadEndpoint {
  private final PresignedUrlService presignedUrlService;

  @PostMapping("/v1/galleries/presigned-urls")
  public PresignedUrlResponse generatePresignedUrl(@RequestBody PresignedUrlRequest presignedUrlRequest) {
    var presignedUrlResponse = presignedUrlService.createPresignedUrls(presignedUrlRequest);

    return presignedUrlResponse;
  }
}