package io.itmca.lifepuzzle.domain.user.endpoint;

import io.itmca.lifepuzzle.domain.register.PasswordVerification;
import io.itmca.lifepuzzle.domain.user.CurrentUser;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserPasswordUpdateRequest;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserUpdateRequest;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import io.itmca.lifepuzzle.global.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserWriteEndpoint {

    private final UserWriteService userWriteService;

    @PatchMapping("/{id}")
    public void updateUser(@PathVariable("id") Long id, @CurrentUser User user, @RequestBody UserUpdateRequest userUpdateRequest) {
        if (id != user.getUserNo()) {
            // exception
        }

        user.updateUserInfo(userUpdateRequest);

        userWriteService.save(user);
    }

    @PatchMapping("/{id}/password")
    public void updateUserPassword(@PathVariable("id") Long id, @CurrentUser User user, @RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest) {
        if (id != user.getUserNo()) {
            // exception
        }

        var isMatch = PasswordUtil.matches(
                PasswordVerification.builder()
                        .plainPassword(userPasswordUpdateRequest.getOldPassword())
                        .salt(user.getSalt())
                        .hashedPassword(user.getPassword())
                        .build()
        );

        if (!isMatch) {
            // exception
        }

        //PasswordChange 클래스 안에 oldPassword, newPassword 래퍼 클래스를 파라미터로 넘기기
        userWriteService.updateUserPassword(user, userPasswordUpdateRequest.getNewPassword());
    }

}
