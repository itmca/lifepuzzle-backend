package io.itmca.lifepuzzle.domain.auth.endpoint;

import io.itmca.lifepuzzle.domain.auth.Login;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.LoginResponse;
import io.itmca.lifepuzzle.domain.auth.service.KakaoValidateService;
import io.itmca.lifepuzzle.domain.auth.service.LoginService;
import io.itmca.lifepuzzle.domain.register.service.SocialRegisterService;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "카카오 로그인")
public class KakaoAuthEndpoint {

  private final LoginService loginService;
  private final UserQueryService userQueryService;
  private final KakaoValidateService kakaoValidateService;
  private final SocialRegisterService socialRegisterService;

  @PostMapping("/auth/social/kakao")
  @Operation(summary = "카카오 로그인")
  public LoginResponse login(@RequestHeader("kakao-access-token") String kakaoAccessToken) {
    var kakaoId = getKakaoId(kakaoAccessToken);

    if (kakaoId != null) {
      return tryKakaoLogin(kakaoId);
    }

    return loginAfterRegistration(kakaoId);
  }

  private LoginResponse tryKakaoLogin(String kakaoId) {
    var kakaoUser = userQueryService.findByKakaoId(kakaoId);

    return loginService.getLoginResponse(
        Login.builder()
            .user(kakaoUser)
            .build());
  }

  private String getKakaoId(String kakaoAccessToken) {
    try {
      return kakaoValidateService.getKakaoIdByTokenValidation(kakaoAccessToken);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private LoginResponse loginAfterRegistration(String kakaoId) {
    socialRegisterService.registerKakaoUser(kakaoId);
    var newKakaoUser = this.userQueryService.findByKakaoId(kakaoId);

    return loginService.getLoginResponse(
        Login.builder()
            .user(newKakaoUser)
            .isNewUser(true)
            .build());
  }
}
