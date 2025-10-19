package io.itmca.lifepuzzle.domain.content.endpoint;

import io.itmca.lifepuzzle.domain.content.endpoint.response.GalleryQueryResponse;
import io.itmca.lifepuzzle.domain.content.service.GalleryQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "갤러리 조회")
public class GalleryQueryEndpoint {
  private final GalleryQueryService galleryQueryService;

  @Deprecated
  @Operation(summary = "홈 화면 조회")
  @GetMapping({"/v1/heroes/{heroId}/gallery"})
  public ResponseEntity<GalleryQueryResponse> getHeroGallery(@PathVariable("heroId") Long heroId) {
    var response = galleryQueryService.getHeroGallery(heroId);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "주인공의 전체 사진 목록 조회")
  @GetMapping({"/v1/galleries"})
  public ResponseEntity<GalleryQueryResponse> getHeroGalleryV2(@RequestParam("heroNo") Long heroNo) {
    var response = galleryQueryService.getHeroGallery(heroNo);
    return ResponseEntity.ok(response);
  }
}
