package io.itmca.lifepuzzle.domain.user.service;

import io.itmca.lifepuzzle.domain.auth.endpoint.request.AppleAuthRequest;
import io.itmca.lifepuzzle.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialRegisterService {

  private final UserWriteService userWriteService;
  private final NicknameProvideService nicknameProvideService;
  private final RegisterPostActionService registerPostActionService;

  public void registerKakaoUser(String kakaoId, String shareKey) {
    var user = userWriteService.save(
        User.builder()
            .loginId("kakao" + kakaoId)
            .nickName(nicknameProvideService.getRandomNickname(kakaoId))
            .kakaoId(kakaoId)
            .build());

    registerPostActionService.doAfterRegisterActions(user, shareKey);
  }

  public void registerAppleUser(AppleAuthRequest appleAuthRequest, String shareKey) {
    var appleUserId = appleAuthRequest.getAppleUserId();
    var email = appleAuthRequest.getEmail();

    var user = userWriteService.save(
        User.builder()
            .loginId("appleUserId" + appleUserId)
            .nickName(nicknameProvideService.getRandomNickname(appleUserId))
            .email(email)
            .appleId(appleUserId)
            .build());

    registerPostActionService.doAfterRegisterActions(user, shareKey);
  }
}
