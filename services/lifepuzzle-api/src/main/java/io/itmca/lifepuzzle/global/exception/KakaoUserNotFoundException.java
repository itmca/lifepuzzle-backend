package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;

public class KakaoUserNotFoundException extends NotFoundException {
  public KakaoUserNotFoundException(String kakaoId) {
    super(String.format("User is not found - kakaoId: %s", kakaoId));
  }
}
