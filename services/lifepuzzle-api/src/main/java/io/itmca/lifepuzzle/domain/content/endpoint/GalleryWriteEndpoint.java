package io.itmca.lifepuzzle.domain.content.endpoint;

import io.itmca.lifepuzzle.domain.content.endpoint.request.GalleryWriteRequest;
import io.itmca.lifepuzzle.domain.content.service.GalleryWriteService;
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

  @Deprecated
  @PostMapping({"/v1/heroes/gallery", // TODO: FE 전환 후 제거
                "/v1/galleries"})
  public void saveGallery(
      @RequestPart List<MultipartFile> gallery,
      @RequestPart(value = "galleryInfo") GalleryWriteRequest galleryWriteRequest) {
    System.out.println("galleryWriteRequest: " + galleryWriteRequest);
    galleryWriteService.saveGallery(galleryWriteRequest.heroId(),
        gallery, galleryWriteRequest.ageGroup());
  }

  @DeleteMapping({"/v2/heroes/gallery/{galleryId}", // TODO: FE 전환 후 제거
                  "/v1/galleries/{galleryId}"})
  public void deleteGalleryItem(@PathVariable Long galleryId) {
    galleryWriteService.deleteGalleryItem(galleryId);
  }
}
