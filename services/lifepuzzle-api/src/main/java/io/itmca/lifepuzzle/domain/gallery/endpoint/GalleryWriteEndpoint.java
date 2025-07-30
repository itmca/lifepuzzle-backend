package io.itmca.lifepuzzle.domain.gallery.endpoint;

import io.itmca.lifepuzzle.domain.gallery.endpoint.request.GalleryWriteRequest;
import io.itmca.lifepuzzle.domain.gallery.service.GalleryWriteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "갤러리 등록")
public class GalleryWriteEndpoint {

  private final GalleryWriteService galleryWriteService;

  @PostMapping({"/v1/heroes/gallery", // TODO: FE 전환 후 제거
                "/v1/galleries"})
  public void saveGallery(
      @RequestPart List<MultipartFile> gallery,
      @RequestPart(value = "galleryInfo") GalleryWriteRequest galleryWriteRequest) {
    galleryWriteService.saveGallery(galleryWriteRequest.getHeroId(),
        gallery, galleryWriteRequest.getAgeGroup());
  }

  @DeleteMapping({"/v2/heroes/gallery/{galleryId}", // TODO: FE 전환 후 제거
                  "/v1/galleries/{galleryId}"})
  public void deleteGalleryItem(@PathVariable Long galleryId) {
    galleryWriteService.deleteGalleryItem(galleryId);
  }
}
