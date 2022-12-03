package io.itmca.lifepuzzle.domain.auth.endpoint;

import io.itmca.lifepuzzle.domain.auth.endpoint.request.LoginRequest;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.LoginResponse;
import io.itmca.lifepuzzle.domain.auth.service.LoginService;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.global.exception.PasswordMismatchException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginEndpoint {

    private final LoginService loginService;
    private final UserQueryService userQueryService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        var username = loginRequest.getUsername();
        var user = userQueryService.findByUserId(username);

        // TO DO: ***REMOVED***-back PaswordUtil 확인해서 salt 적용 및 기존 비밀번호랑 DB에 있는 것 참고해서 잘 동작하는지 테스트 코드 만들기
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new PasswordMismatchException();
        }

        return this.loginService.getLoginResponse(username);
    }
}
