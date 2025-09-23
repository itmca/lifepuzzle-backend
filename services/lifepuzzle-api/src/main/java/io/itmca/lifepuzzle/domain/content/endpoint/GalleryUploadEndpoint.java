package io.itmca.lifepuzzle.domain.content.endpoint;

import io.itmca.lifepuzzle.domain.content.endpoint.request.GalleryUploadCompleteRequest;
import io.itmca.lifepuzzle.domain.content.endpoint.request.PresignedUrlRequest;
import io.itmca.lifepuzzle.domain.content.endpoint.response.GalleryUploadCompleteResponse;
import io.itmca.lifepuzzle.domain.content.endpoint.response.PresignedUrlResponse;
import io.itmca.lifepuzzle.domain.content.service.GalleryWriteService;
import io.itmca.lifepuzzle.domain.content.service.PresignedUrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Gallery Upload")
public class GalleryUploadEndpoint {
  private final PresignedUrlService presignedUrlService;
  private final GalleryWriteService galleryWriteService;

  @PostMapping("/v1/galleries/presigned-urls")
  @Operation(summary = "Presigned URL 생성", description = "S3 파일 업로드를 위한 presigned URL을 생성합니다")
  public PresignedUrlResponse generatePresignedUrl(@Valid @RequestBody PresignedUrlRequest presignedUrlRequest) {
    return presignedUrlService.createPresignedUrls(presignedUrlRequest);
  }

  @PostMapping("/v1/galleries/upload-complete")
  @Operation(summary = "업로드 완료 처리", description = "S3 업로드 완료 후 갤러리 상태를 업데이트합니다")
  public GalleryUploadCompleteResponse completeUpload(@Valid @RequestBody GalleryUploadCompleteRequest request) {
    return galleryWriteService.completeUploads(request.fileKeys());
  }
}