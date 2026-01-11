package io.itmca.lifepuzzle.domain.auth.endpoint;

import io.itmca.lifepuzzle.domain.auth.endpoint.request.FacebookPhotoImportRequest;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.FacebookDataDeletionResponse;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.FacebookDataDeletionStatusResponse;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.FacebookPhotoImportResponse;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.FacebookPhotosResponse;
import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.auth.service.FacebookDataDeletionService;
import io.itmca.lifepuzzle.domain.auth.service.FacebookOAuthService;
import io.itmca.lifepuzzle.domain.auth.service.FacebookPhotoImportService;
import io.itmca.lifepuzzle.domain.auth.service.FacebookPhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@Tag(name = "페이스북 OAuth")
public class FacebookOAuthEndpoint {
  private final FacebookOAuthService facebookOAuthService;
  private final FacebookPhotoService facebookPhotoService;
  private final FacebookDataDeletionService facebookDataDeletionService;
  private final FacebookPhotoImportService facebookPhotoImportService;

  @Operation(summary = "Facebook 사진 목록 조회")
  @GetMapping("/v1/facebook/photos")
  public ResponseEntity<FacebookPhotosResponse> getFacebookPhotos(
      @RequestParam String code,
      @AuthenticationPrincipal AuthPayload authPayload
  ) {
    if (authPayload == null) {
      throw new AccessDeniedException("Authentication is required.");
    }
    var accessToken = facebookOAuthService.getAccessToken(code);
    var response = facebookPhotoService.getFilteredUserPhotos(accessToken);

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Facebook 사진 1회성 가져오기", description = "Facebook 사진을 가져와 갤러리에 저장합니다.")
  @PostMapping("/v1/facebook/photos/import")
  public ResponseEntity<FacebookPhotoImportResponse> importFacebookPhotos(
      @RequestBody FacebookPhotoImportRequest request,
      @AuthenticationPrincipal AuthPayload authPayload
  ) {
    if (authPayload == null) {
      throw new AccessDeniedException("Authentication is required.");
    }

    var response = facebookPhotoImportService.importPhotos(
        authPayload.getUserId(),
        request.code(),
        request.heroNo(),
        request.ageGroup()
    );

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Facebook OAuth 콜백", description = "Facebook OAuth 인증 완료 후 호출되는 콜백 엔드포인트입니다. 클라이언트에서 URL의 code 파라미터를 파싱하여 사용합니다.")
  @GetMapping("/v1/facebook/callback")
  public ResponseEntity<String> facebookOAuthCallback(@RequestParam String code) {
    return ResponseEntity.ok("Facebook OAuth 인증이 완료되었습니다.");
  }

  @Operation(summary = "Facebook 데이터 삭제 콜백", description = "Facebook 데이터 삭제 요청을 처리하고 확인 코드를 반환합니다.")
  @PostMapping(value = "/v1/facebook/data-deletion", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<FacebookDataDeletionResponse> facebookDataDeletionCallback(
      @RequestParam("signed_request") String signedRequest
  ) {
    var confirmationCode = facebookDataDeletionService.processSignedRequest(signedRequest);
    var statusUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/v1/facebook/data-deletion/status")
        .queryParam("code", confirmationCode)
        .toUriString();
    var response = new FacebookDataDeletionResponse(statusUrl, confirmationCode);

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Facebook 데이터 삭제 상태 조회", description = "Facebook 데이터 삭제 요청 상태를 확인합니다.")
  @GetMapping("/v1/facebook/data-deletion/status")
  public ResponseEntity<FacebookDataDeletionStatusResponse> facebookDataDeletionStatus(
      @RequestParam("code") String code
  ) {
    return ResponseEntity.ok(new FacebookDataDeletionStatusResponse(code, "received"));
  }
}
