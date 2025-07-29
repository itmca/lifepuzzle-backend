package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;

public class UserNotFoundException extends NotFoundException {

  private UserNotFoundException(String msg) {
    super(msg);
  }

  public static UserNotFoundException byUserNo(Long userNo) {
    return new UserNotFoundException(String.format("User is not found - userNo: %d", userNo));
  }

  public static UserNotFoundException byId(String userId) {
    return new UserNotFoundException(String.format("User is not found - userId: %s", userId));
  }

  public static UserNotFoundException byEmail(String email) {
    return new UserNotFoundException(String.format("User is not found - email: %s", email));
  }
}
