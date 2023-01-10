package io.itmca.lifepuzzle.domain.register.service;

import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import io.itmca.lifepuzzle.global.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserWriteService userWriteService;
    private final RegisterPostActionService registerPostActionService;
    private final PasswordEncoder passwordEncoder;

    @Async
    public void register(User user) {
        var registeredUser = this.registerInternally(user);

        registerPostActionService.doAfterRegisterActions(registeredUser);
    }

    @Async
    private User registerInternally(User user) {
        this.setSaltAndEncodedPasswordToUser(user);

        return this.userWriteService.save(user);
    }

    private void setSaltAndEncodedPasswordToUser(User user) {
        var salt = PasswordUtil.genSalt();
        var originPassword = user.getPassword();
        var hashedPassword = PasswordUtil.hashPassword(originPassword, salt);

        user.setSaltAndEncodedPassword(salt, hashedPassword);

        if (!isPasswordCorrectlyGenerated(user, originPassword)) {
            // exception
        }
    }

    private Boolean isPasswordCorrectlyGenerated(User user, String originPassword) {
        return passwordEncoder.matches(originPassword + user.getSalt(), user.getPassword());
//        return PasswordUtil.matches(PasswordVerification.builder()
//                .plainPassword(originPassword)
//                .salt(user.getSalt())
//                .hashedPassword(user.getPassword())
//                .build());
    }
}
