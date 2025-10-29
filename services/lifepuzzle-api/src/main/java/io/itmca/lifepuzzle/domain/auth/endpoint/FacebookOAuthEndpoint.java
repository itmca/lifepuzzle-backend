package io.itmca.lifepuzzle.domain.auth.endpoint;

import io.itmca.lifepuzzle.domain.auth.endpoint.response.FacebookPhotosResponse;
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
  @GetMapping("/v1/facebook/photos")
  public ResponseEntity<FacebookPhotosResponse> getFacebookPhotos(@RequestParam String code) {
    var accessToken = facebookOAuthService.getAccessToken(code);
    var response = facebookPhotoService.getFilteredUserPhotos(accessToken);

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Facebook OAuth 콜백", description = "Facebook OAuth 인증 완료 후 호출되는 콜백 엔드포인트입니다. 클라이언트에서 URL의 code 파라미터를 파싱하여 사용합니다.")
  @GetMapping("/v1/facebook/callback")
  public ResponseEntity<String> facebookOAuthCallback(@RequestParam String code) {
    return ResponseEntity.ok("Facebook OAuth 인증이 완료되었습니다.");
  }
}
