package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;

public class UserNotFoundException extends NotFoundException {

  private UserNotFoundException(String msg) {
    super(msg);
  }

  public static UserNotFoundException notFoundByUserNo(Long userNo) {
    return new UserNotFoundException(String.format("User is not found - userNo: %d", userNo));
  }

  public static UserNotFoundException notFoundById(String userId) {
    return new UserNotFoundException(String.format("User is not found - userId: %s", userId));
  }

  public static UserNotFoundException notFoundByEmail(String email) {
    return new UserNotFoundException(String.format("User is not found - email: %s", email));
  }

  public static UserNotFoundException notFoundByKakaoId(String kakaoId) {
    return new UserNotFoundException(String.format("User is not found - kakaoId: %s", kakaoId));
  }

  public static UserNotFoundException notFoundByAppleId(String appleId) {
    return new UserNotFoundException(String.format("User is not found - appleId: %s", appleId));
  }

}
