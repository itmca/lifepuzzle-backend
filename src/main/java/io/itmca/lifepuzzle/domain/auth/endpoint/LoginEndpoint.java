package io.itmca.lifepuzzle.domain.auth.endpoint;

import io.itmca.lifepuzzle.domain.auth.Login;
import io.itmca.lifepuzzle.domain.auth.endpoint.request.LoginRequest;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.LoginResponse;
import io.itmca.lifepuzzle.domain.auth.service.LoginService;
import io.itmca.lifepuzzle.domain.register.PasswordVerification;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.global.exception.PasswordMismatchException;
import io.itmca.lifepuzzle.global.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginEndpoint {

    private final LoginService loginService;
    private final UserQueryService userQueryService;

    @GetMapping("/")
    public String HealthCheck() {
        return "OK";
    }

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        var username = loginRequest.getUsername();
        var user = userQueryService.findByUserId(username);

        PasswordVerification passwordVerification = PasswordVerification.builder()
                .plainPassword(loginRequest.getPassword())
                .salt(user.getSalt())
                .hashedPassword(user.getPassword())
                .build();

        // TO DO: ***REMOVED***-back PasswordUtil 확인해서 salt 적용 및 기존 비밀번호랑 DB에 있는 것 참고해서 잘 동작하는지 테스트 코드 만들기
        if (!PasswordUtil.matches(passwordVerification)) {
            throw new PasswordMismatchException();
        }

        return loginService.getLoginResponse(
                Login.builder()
                        .user(user)
                        .build());
    }
}

