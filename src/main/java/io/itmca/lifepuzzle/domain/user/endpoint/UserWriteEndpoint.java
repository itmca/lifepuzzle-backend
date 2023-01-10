package io.itmca.lifepuzzle.domain.user.endpoint;

import io.itmca.lifepuzzle.domain.user.CurrentUser;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserPasswordUpdateRequest;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserUpdateRequest;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserWriteEndpoint {

    private final UserQueryService userQueryService;
    private final UserWriteService userWriteService;
    private final PasswordEncoder passwordEncoder;

    @PatchMapping("/{id}")
    public void updateUser(@PathVariable("id") Long id, @CurrentUser User user, @RequestBody UserUpdateRequest userUpdateRequest) {
        if (id != user.getUserNo()) {
            // exception
        }

        userWriteService.update(user, userUpdateRequest);
    }

    @PatchMapping("/{id}/password")
    public void updateUserPassword(@PathVariable("id") Long id, @CurrentUser User user, @RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest) {
        if (id != user.getUserNo()) {
            // exception
        }

        var isMatch = passwordEncoder.matches(userPasswordUpdateRequest.getOldPassword() + user.getSalt(), user.getPassword());

        if (!isMatch) {
            // exception
        }

        var newPassword = passwordEncoder.encode(userPasswordUpdateRequest.getNewPassword());

        userWriteService.updateUserPassword(user, newPassword);
    }

}
