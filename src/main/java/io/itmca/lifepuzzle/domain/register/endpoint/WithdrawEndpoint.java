package io.itmca.lifepuzzle.domain.register.endpoint;

import io.itmca.lifepuzzle.domain.register.service.WithdrawService;
import io.itmca.lifepuzzle.domain.user.CurrentUser;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WithdrawEndpoint {

    private final WithdrawService withdrawService;

    @DeleteMapping("/users/{id}")
    public void withdraw(@PathVariable("id") Long id, @CurrentUser User user, @RequestBody String socialToken) {
        if (id != user.getUserNo()) {
            throw new UserNotFoundException(id);
        }

        withdrawService.withdraw(user, socialToken);
    }
}
