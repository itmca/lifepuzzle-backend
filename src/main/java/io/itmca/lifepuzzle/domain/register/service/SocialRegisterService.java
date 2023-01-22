package io.itmca.lifepuzzle.domain.register.service;

import io.itmca.lifepuzzle.domain.auth.endpoint.request.AppleAuthBody;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialRegisterService {

    private final UserWriteService userWriteService;
    private final NicknameProvideService nicknameProvideService;
    private final RegisterPostActionService registerPostActionService;

    public void registerKakaoUser(String kakaoId) {
        var user = userWriteService.save(
                User.builder()
                        .userId("kakao" + kakaoId)
                        .nickName(nicknameProvideService.getRandomNickname(kakaoId))
                        .kakaoId(kakaoId)
                        .build());

        registerPostActionService.doAfterRegisterActions(user);
    }

    public void registerAppleUser(AppleAuthBody appleAuthBody) {
        var appleUserId = appleAuthBody.getAppleUserId();
        var email = appleAuthBody.getEmail();

        var user = userWriteService.save(
                User.builder()
                        .userId("appleUserId" + appleUserId)
                        .nickName(nicknameProvideService.getRandomNickname(appleUserId))
                        .email(email)
                        .appleId(appleUserId)
                        .build());

        registerPostActionService.doAfterRegisterActions(user);
    }
}
