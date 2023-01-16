package io.itmca.lifepuzzle.domain.register.service;

import io.itmca.lifepuzzle.domain.register.PasswordVerification;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import io.itmca.lifepuzzle.global.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserWriteService userWriteService;
    private final RegisterPostActionService registerPostActionService;

    @Async
    public void register(User user) {
        var registeredUser = this.registerInternally(user);

        registerPostActionService.doAfterRegisterActions(registeredUser);
    }

    private User registerInternally(User user) {
        this.setSaltAndEncodedPasswordToUser(user);

        return this.userWriteService.save(user);
    }

    private void setSaltAndEncodedPasswordToUser(User user) {
        var originPassword = user.getPassword();
        user.hashCredential(originPassword);

        if (!isPasswordCorrectlyGenerated(user, originPassword)) {
        }
    }

    private Boolean isPasswordCorrectlyGenerated(User user, String originPassword) {
        return PasswordUtil.matches(PasswordVerification.builder()
                .plainPassword(originPassword)
                .salt(user.getSalt())
                .hashedPassword(user.getPassword())
                .build());
    }
}
