package io.itmca.lifepuzzle.domain.auth.endpoint;

import io.itmca.lifepuzzle.domain.auth.endpoint.request.LoginRequest;
import io.itmca.lifepuzzle.domain.auth.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginEndpoint {

    private LoginService loginService;

    public LoginEndpoint(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return this.loginService.getLoginResponse(loginRequest.getUserId());
    }
}
