package io.itmca.lifepuzzle.domain.register.endpoint;

import io.itmca.lifepuzzle.domain.register.endpoint.request.UserRegisterRequest;
import io.itmca.lifepuzzle.domain.register.service.NicknameProvideService;
import io.itmca.lifepuzzle.domain.register.service.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원가입")
public class RegisterEndpoint {

  private final RegisterService registerService;
  private final NicknameProvideService nicknameProvideService;

  @PostMapping("/user")
  @Operation(summary = "회원가입")
  public HttpStatus register(@RequestBody UserRegisterRequest userRegisterRequest) {
    var user = userRegisterRequest.toUser();

    if (!StringUtils.hasText(user.getNickName())) {
      user.setRandomNickname(nicknameProvideService.getRandomNickname(user.getUserId()));
    }

    registerService.register(user);

    return HttpStatus.OK;
  }
}
