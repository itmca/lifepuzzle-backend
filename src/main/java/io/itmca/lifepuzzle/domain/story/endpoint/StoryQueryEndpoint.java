package io.itmca.lifepuzzle.domain.story.endpoint;

import io.itmca.lifepuzzle.domain.story.endpoint.response.GalleryQueryResponse;
import io.itmca.lifepuzzle.domain.story.service.StoryPhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "스토리 조회")
public class StoryQueryEndpoint {
  private final StoryPhotoService storyPhotoService;

  @Operation(summary = "홈 화면 조회")
  @GetMapping("/v1/heroes/{heroId}/gallery")
  public ResponseEntity<GalleryQueryResponse> getHeroGallery(@PathVariable("heroId") Long heroId) {
    var response = storyPhotoService.getHeroGallery(heroId);
    return ResponseEntity.ok(response);
  }
}
