package io.itmca.lifepuzzle.domain.auth.endpoint;

import io.itmca.lifepuzzle.domain.auth.Login;
import io.itmca.lifepuzzle.domain.auth.endpoint.request.AppleAuthBody;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.LoginResponse;
import io.itmca.lifepuzzle.domain.auth.service.AppleValidateService;
import io.itmca.lifepuzzle.domain.auth.service.LoginService;
import io.itmca.lifepuzzle.domain.register.service.SocialRegisterService;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class AppleAuthController {

    private final AppleValidateService appleValidateService;
    private final UserQueryService userQueryService;
    private final LoginService loginService;
    private final SocialRegisterService socialRegisterService;

    @PostMapping("/auth/social/apple")
    public LoginResponse login(@RequestBody AppleAuthBody appleAuthBody) throws ParseException {
        verify(appleAuthBody);

        var appleUser = userQueryService.findByAppleId(appleAuthBody.getAppleUserId());
        var loginType = Login.builder()
                .user(appleUser)
                .build();

        return appleUser != null
                ? loginService.getLoginResponse(loginType)
                : loginAfterRegistration(appleAuthBody);

    }

    private void verify(AppleAuthBody appleAuthBody) {
        String sub = "";
        try {
            sub = appleValidateService.parseToken(appleAuthBody.getIdentityToken());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (!sub.equals(appleAuthBody.getAppleUserId())) {
        }
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
