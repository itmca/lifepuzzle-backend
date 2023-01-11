package io.itmca.lifepuzzle.domain.register.endpoint;

import io.itmca.lifepuzzle.domain.register.endpoint.request.UserRegisterRequest;
import io.itmca.lifepuzzle.domain.register.service.NicknameProvideService;
import io.itmca.lifepuzzle.domain.register.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterEndpoint {

    private final RegisterService registerService;
    private final NicknameProvideService nicknameProvideService;

    @PostMapping("/user")
    public HttpStatus register(@RequestBody UserRegisterRequest userRegisterRequest) {
        var user = userRegisterRequest.toUser();

        if (!StringUtils.hasText(user.getNickName())) {
            user.setRandomNickname(nicknameProvideService.getRandomNickname(user.getUserId()));
        }

        registerService.register(user);

        return HttpStatus.OK;
    }
}
