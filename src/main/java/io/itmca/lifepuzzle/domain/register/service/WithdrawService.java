package io.itmca.lifepuzzle.domain.register.service;

import io.itmca.lifepuzzle.domain.auth.ApplePayload;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final UserQueryService userQueryService;
    private final UserWriteService userWriteService;

    public void withdraw(User user, String socialToken) {
        if (user.getUserType().equals("apple") && StringUtils.hasText(socialToken)) {
            revokeAppleToken(socialToken);
        }

        userWriteService.deleteByUserNo(user.getUserNo());
    }

    private void revokeAppleToken(String socialToken) {

    }

    private void getAppleSecret() {
        var appleTeamId = "***REMOVED***";
        var appleBundleId = "io.itmca.lifepuzzle";
        var applePrivateKeyId = "***REMOVED***";

        var nowInSeconds = Instant.now().getEpochSecond();
        var durationInSeconds = 60 * 5;

        ApplePayload applePayload = ApplePayload.builder()
                .iss(appleTeamId)
                .iat(nowInSeconds)
                .exp(nowInSeconds + durationInSeconds)
                .aud("https://appleid.apple.com")
                .sub(appleBundleId)
                .build();

    }

    private String getApplePrivateKey() {
        return "***REMOVED***" +
                "***REMOVED***" +
                "***REMOVED***" +
                "***REMOVED***" +
                "***REMOVED***" +
                "***REMOVED***";
    }


}
