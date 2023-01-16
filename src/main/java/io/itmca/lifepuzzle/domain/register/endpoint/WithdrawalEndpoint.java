package io.itmca.lifepuzzle.domain.register.endpoint;

import io.itmca.lifepuzzle.domain.register.service.WithdrawService;
import io.itmca.lifepuzzle.domain.user.CurrentUser;
import io.itmca.lifepuzzle.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WithdrawalEndpoint {

    private final WithdrawService withdrawService;

    @DeleteMapping("/users/{id}")
    public void withdraw(@PathVariable("id") Long id, @CurrentUser User user, @RequestBody String socialToken) {
        if (id != user.getUserNo()) {
        }

        withdrawService.withdraw(user, socialToken);
    }
}
