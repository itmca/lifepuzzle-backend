package io.itmca.lifepuzzle.domain.auth.endpoint;

import io.itmca.lifepuzzle.domain.auth.endpoint.response.FacebookPhotoResponse;
import io.itmca.lifepuzzle.domain.auth.service.FacebookOAuthService;
import io.itmca.lifepuzzle.domain.auth.service.FacebookPhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "페이스북 OAuth")
public class FacebookOAuthEndpoint {
  private final FacebookOAuthService facebookOAuthService;
  private final FacebookPhotoService facebookPhotoService;

  @Operation(summary = "Facebook 사진 목록 조회")
  @GetMapping("/facebook/photos")
  public ResponseEntity<FacebookPhotoResponse> getFacebookPhotos(@RequestParam String code) {
    var accessToken = facebookOAuthService.getAccessToken(code);
    var response = facebookPhotoService.getUserPhotos(accessToken);

    return ResponseEntity.ok(response);
  }
}
