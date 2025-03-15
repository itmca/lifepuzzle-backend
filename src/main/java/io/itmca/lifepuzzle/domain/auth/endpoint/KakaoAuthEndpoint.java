package io.itmca.lifepuzzle.domain.auth.endpoint;

import ch.qos.logback.core.util.StringUtil;
import io.itmca.lifepuzzle.domain.auth.Login;
import io.itmca.lifepuzzle.domain.auth.endpoint.request.KakaoAuthRequest;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.LoginResponse;
import io.itmca.lifepuzzle.domain.auth.service.KakaoValidateService;
import io.itmca.lifepuzzle.domain.auth.service.LoginService;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.user.service.SocialRegisterService;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  private final HeroUserAuthWriteService heroUserAuthWriteService;

  @Operation(summary = "카카오 로그인")
  @PostMapping({"/auth/social/kakao", // TODO: FE 전환 후 제거
      "/auth/login/kakao"})
  public LoginResponse login(@RequestHeader("kakao-access-token") String kakaoAccessToken,
                             @RequestBody KakaoAuthRequest kakaoAuthRequest) {
    var kakaoId = getKakaoId(kakaoAccessToken);
    var shareKey = kakaoAuthRequest.getShareKey();
    try {
      return tryKakaoLogin(kakaoId, shareKey);
    } catch (NotFoundException e) {
      return loginAfterRegistration(kakaoId, shareKey);
    }
  }

  private LoginResponse tryKakaoLogin(String kakaoId, String shareKey) throws NotFoundException {
    var kakaoUser = userQueryService.findByKakaoId(kakaoId);

    if (StringUtil.notNullNorEmpty(shareKey)) {
      heroUserAuthWriteService.createByShareKey(kakaoUser, shareKey);

    }

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

  private LoginResponse loginAfterRegistration(String kakaoId, String shareKey) {
    socialRegisterService.registerKakaoUser(kakaoId, shareKey);
    var newKakaoUser = this.userQueryService.findByKakaoId(kakaoId);

    return loginService.getLoginResponse(
        Login.builder()
            .user(newKakaoUser)
            .isNewUser(true)
            .build());
  }
}
