package io.itmca.lifepuzzle.domain.register.service;

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

    public User registerKakaoUser(String kakaoId) {
        var user = userWriteService.save(
                User.builder()
                        .userId(kakaoId)
                        .nickName(nicknameProvideService.getRandomNickname(kakaoId))
                        .kakaoId(kakaoId)
                        .build());

        registerPostActionService.doAfterRegisterActions(user);

        return user;
    }
}
