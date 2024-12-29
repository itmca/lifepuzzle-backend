package io.itmca.lifepuzzle.domain.register.endpoint;

import io.itmca.lifepuzzle.domain.register.endpoint.request.UserRegisterRequest;
import io.itmca.lifepuzzle.domain.register.service.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원가입")
public class RegisterEndpoint {

  private final RegisterService registerService;

  @Operation(summary = "회원가입")
  @PostMapping({"/user", // TODO: FE 전환 후 제거
      "/users"})
  public HttpStatus register(@RequestBody UserRegisterRequest userRegisterRequest) {
    var user = userRegisterRequest.toUser();

    registerService.register(user, userRegisterRequest.getShareKey());

    return HttpStatus.OK;
  }
}
