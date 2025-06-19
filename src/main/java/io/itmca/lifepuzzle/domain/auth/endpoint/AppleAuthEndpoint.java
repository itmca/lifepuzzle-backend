package io.itmca.lifepuzzle.domain.auth.endpoint;

import ch.qos.logback.core.util.StringUtil;
import io.itmca.lifepuzzle.domain.auth.Login;
import io.itmca.lifepuzzle.domain.auth.endpoint.request.AppleAuthRequest;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.LoginResponse;
import io.itmca.lifepuzzle.domain.auth.service.AppleValidateService;
import io.itmca.lifepuzzle.domain.auth.service.LoginService;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.user.service.SocialRegisterService;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "애플 로그인")
public class AppleAuthEndpoint {

  private final AppleValidateService appleValidateService;
  private final UserQueryService userQueryService;
  private final LoginService loginService;
  private final SocialRegisterService socialRegisterService;
  private final HeroUserAuthWriteService heroUserAuthWriteService;

  @Operation(summary = "애플 로그인")
  @PostMapping({"/auth/login/apple"})
  public LoginResponse login(@RequestBody AppleAuthRequest appleAuthRequest) throws ParseException {
    log.info("Apple Body : {}", appleAuthRequest);
    verify(appleAuthRequest);
    var shareKey = appleAuthRequest.getShareKey();
    try {
      return tryAppleLogin(appleAuthRequest, shareKey);
    } catch (NotFoundException e) {
      return loginAfterRegistration(appleAuthRequest, shareKey);
    }
  }

  private LoginResponse tryAppleLogin(AppleAuthRequest appleAuthRequest, String shareKey)
      throws NotFoundException {
    var appleUser = userQueryService.findByAppleId(appleAuthRequest.getAppleUserId());
    var loginType = Login.builder()
        .user(appleUser)
        .build();

    if (StringUtil.notNullNorEmpty(shareKey)) {
      heroUserAuthWriteService.createByShareKey(appleUser, shareKey);
    }

    return loginService.getLoginResponse(loginType);
  }

  private void verify(AppleAuthRequest appleAuthRequest) throws ParseException {
    var sub = appleValidateService.parseToken(appleAuthRequest.getIdentityToken());
  }

  private LoginResponse loginAfterRegistration(AppleAuthRequest appleAuthRequest, String shareKey) {
    socialRegisterService.registerAppleUser(appleAuthRequest, shareKey);
    var newAppleUser = userQueryService.findByAppleId(appleAuthRequest.getAppleUserId());
    var appleIdentityToken = appleAuthRequest.getIdentityToken();

    return loginService.getLoginResponse(
        Login.builder()
            .user(newAppleUser)
            .socialToken(appleIdentityToken)
            .isNewUser(true)
            .build());
  }
}
