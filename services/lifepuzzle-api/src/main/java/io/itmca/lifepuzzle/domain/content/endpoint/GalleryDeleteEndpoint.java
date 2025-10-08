package io.itmca.lifepuzzle.domain.content.endpoint;

import io.itmca.lifepuzzle.domain.content.service.GalleryWriteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "갤러리 삭제")
public class GalleryDeleteEndpoint {

  private final GalleryWriteService galleryWriteService;

  @DeleteMapping("/v1/galleries/{galleryId}")
  public void deleteGalleryItem(@PathVariable Long galleryId) {
    galleryWriteService.deleteGalleryItem(galleryId);
  }
}
