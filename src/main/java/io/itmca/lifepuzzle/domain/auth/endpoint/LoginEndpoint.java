package io.itmca.lifepuzzle.domain.auth.endpoint;

import ch.qos.logback.core.util.StringUtil;
import io.itmca.lifepuzzle.domain.auth.Login;
import io.itmca.lifepuzzle.domain.auth.endpoint.request.LoginRequest;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.LoginResponse;
import io.itmca.lifepuzzle.domain.auth.service.LoginService;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.user.model.PasswordVerification;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.global.exception.PasswordMismatchException;
import io.itmca.lifepuzzle.global.util.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "로그인")
public class LoginEndpoint {

  private final LoginService loginService;
  private final UserQueryService userQueryService;
  private final HeroUserAuthWriteService heroUserAuthWriteService;

  @PostMapping({"/auth/login/email"})
  @Operation(summary = "일반 로그인")
  public LoginResponse login(@RequestBody LoginRequest loginRequest) {
    var username = loginRequest.getUsername();
    var user = userQueryService.findByLoginId(username);

    var passwordVerification = PasswordVerification.builder()
        .plainPassword(loginRequest.getPassword())
        .salt(user.getSalt())
        .hashedPassword(user.getPassword())
        .build();

    if (!PasswordUtil.matches(passwordVerification)) {
      throw new PasswordMismatchException();
    }

    var shareKey = loginRequest.getShareKey();
    if (StringUtil.notNullNorEmpty(shareKey)) {
      heroUserAuthWriteService.createByShareKey(user, shareKey);
    }
    return loginService.getLoginResponse(
        Login.builder()
            .user(user)
            .build());
  }
}