package io.itmca.lifepuzzle.domain.auth.endpoint;

import io.itmca.lifepuzzle.domain.auth.endpoint.response.FacebookPhotosResponse;
import io.itmca.lifepuzzle.domain.auth.service.FacebookOAuthService;
import io.itmca.lifepuzzle.domain.auth.service.FacebookPhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

  @Operation(summary = "Facebook 사진 딥링크 리다이렉트", description = "Facebook OAuth 인증 코드를 받아 lifepuzzle://facebook/photo 딥링크로 리다이렉트합니다.")
  @GetMapping("/v1/facebook/photos/callback")
  public ResponseEntity<Void> redirectToPhotoDeeplink(@RequestParam String code) {
    String deeplinkUrl = "lifepuzzle://facebook/photos?code=" + code;

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(deeplinkUrl));

    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }
}
