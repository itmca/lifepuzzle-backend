package io.itmca.lifepuzzle.domain.story.endpoint;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.story.endpoint.response.LikeWriteResponse;
import io.itmca.lifepuzzle.domain.story.service.StoryLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "좋아요")
public class LikeWriteEndpoint {

  private final StoryLikeService storyLikeService;

  @Operation(summary = "스토리 좋아요")
  @PostMapping("/v1/stories/{storyKey}/likes")
  public ResponseEntity<LikeWriteResponse> addLikeToStory(@PathVariable("storyKey") String storyKey,
                                                          @AuthenticationPrincipal
                                                          AuthPayload authPayload) {
    var likeWriteResponse = storyLikeService.addLike(storyKey, authPayload.getUserId());

    return ResponseEntity.ok(likeWriteResponse);
  }

  @Operation(summary = "스토리 좋아요 취소")
  @DeleteMapping("/v1/stories/{storyKey}/likes")
  public ResponseEntity<LikeWriteResponse> removeLikeFromStory(
      @PathVariable("storyKey") String storyKey,
      @AuthenticationPrincipal AuthPayload authPayload) {
    var likeWriteResponse = storyLikeService.deleteLike(storyKey, authPayload.getUserId());

    return ResponseEntity.ok(likeWriteResponse);
  }
}
