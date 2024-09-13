package io.itmca.lifepuzzle.domain.user.service;

import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.repository.UserRepository;
import io.itmca.lifepuzzle.global.exception.AppleUserNotFoundException;
import io.itmca.lifepuzzle.global.exception.KakaoUserNotFoundException;
import io.itmca.lifepuzzle.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {

  private final UserRepository userRepository;

  public User findByUserNo(long userNo) {
    return userRepository.findById(userNo)
        .orElseThrow(() -> UserNotFoundException.byUserNo(userNo));
  }

  public User findByLoginId(String userLoginId) {
    return userRepository.findByLoginId(userLoginId)
        .orElseThrow(() -> UserNotFoundException.byId(userLoginId));
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> UserNotFoundException.byEmail(email));
  }

  public User findByKakaoId(String kakaoId) {
    return userRepository.findByKakaoId(kakaoId)
        .orElseThrow(() -> new KakaoUserNotFoundException(kakaoId));
  }

  public User findByAppleId(String appleId) {
    return userRepository.findByAppleId(appleId)
        .orElseThrow(() -> new AppleUserNotFoundException(appleId));
  }
}
