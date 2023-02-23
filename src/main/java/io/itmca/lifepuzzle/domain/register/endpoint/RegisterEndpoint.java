package io.itmca.lifepuzzle.domain.register.endpoint;

import io.itmca.lifepuzzle.domain.register.endpoint.request.UserRegisterRequest;
import io.itmca.lifepuzzle.domain.register.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterEndpoint {

    private final RegisterService registerService;


    @PostMapping("/user")
    public HttpStatus register(@RequestBody UserRegisterRequest userRegisterRequest) {
        var user = userRegisterRequest.toUser();

        registerService.register(user);

        return HttpStatus.OK;
    }
}
