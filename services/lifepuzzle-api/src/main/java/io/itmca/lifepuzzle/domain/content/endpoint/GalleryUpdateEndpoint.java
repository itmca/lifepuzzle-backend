package io.itmca.lifepuzzle.domain.content.endpoint;

import io.itmca.lifepuzzle.domain.content.endpoint.request.GalleryUpdateRequest;
import io.itmca.lifepuzzle.domain.content.entity.Gallery;
import io.itmca.lifepuzzle.domain.content.service.GalleryWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Gallery Update")
public class GalleryUpdateEndpoint {
  private final GalleryWriteService galleryWriteService;

  @PatchMapping("/v1/galleries/{galleryId}")
  @Operation(summary = "갤러리 메타데이터 업데이트", description = "갤러리의 날짜와 나이 그룹을 업데이트합니다")
  public ResponseEntity<Void> updateGalleryMetadata(
      @PathVariable Long galleryId,
      @Valid @RequestBody GalleryUpdateRequest request) {
    galleryWriteService.updateGalleryMetadata(galleryId, request.ageGroup(), request.date());
    return ResponseEntity.ok().build();
  }
}
