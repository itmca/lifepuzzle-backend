package io.itmca.lifepuzzle.domain.register.service;

import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final UserQueryService userQueryService;
    private final UserWriteService userWriteService;

    public void withdraw(User user, String socialToken) {
        if (user.getUserType().equals("apple") && StringUtils.hasText(socialToken)) {
        }

        userWriteService.deleteByUserNo(user.getUserNo());
    }

    private void revokeAppleToken(String socialToken) {

    }


}
