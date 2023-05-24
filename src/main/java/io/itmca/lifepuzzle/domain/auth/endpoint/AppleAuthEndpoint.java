package io.itmca.lifepuzzle.domain.auth.endpoint;

import io.itmca.lifepuzzle.domain.auth.Login;
import io.itmca.lifepuzzle.domain.auth.endpoint.request.AppleAuthBody;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.LoginResponse;
import io.itmca.lifepuzzle.domain.auth.service.AppleValidateService;
import io.itmca.lifepuzzle.domain.auth.service.LoginService;
import io.itmca.lifepuzzle.domain.register.service.SocialRegisterService;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "애플 로그인")
public class AppleAuthEndpoint {

  private final AppleValidateService appleValidateService;
  private final UserQueryService userQueryService;
  private final LoginService loginService;
  private final SocialRegisterService socialRegisterService;

  @PostMapping("/auth/social/apple")
  @Operation(summary = "애플 로그인")
  public LoginResponse login(@RequestBody AppleAuthBody appleAuthBody) throws ParseException {
    verify(appleAuthBody);
    return tryAppleLogin(appleAuthBody);
  }

  private LoginResponse tryAppleLogin(AppleAuthBody appleAuthBody) {
    try {
      var appleUser = userQueryService.findByAppleId(appleAuthBody.getAppleUserId());
      var loginType = Login.builder()
          .user(appleUser)
          .build();
      return loginService.getLoginResponse(loginType);
    } catch (NotFoundException e) {
      return loginAfterRegistration(appleAuthBody);
    }
  }

  private void verify(AppleAuthBody appleAuthBody) throws ParseException {
    var sub = appleValidateService.parseToken(appleAuthBody.getIdentityToken());
  }

  private LoginResponse loginAfterRegistration(AppleAuthBody appleAuthBody) {
    socialRegisterService.registerAppleUser(appleAuthBody);
    var newAppleUser = userQueryService.findByAppleId(appleAuthBody.getAppleUserId());
    var appleIdentityToken = appleAuthBody.getIdentityToken();

    return loginService.getLoginResponse(
        Login.builder()
            .user(newAppleUser)
            .socialToken(appleIdentityToken)
            .isNewUser(true)
            .build());
  }
}
