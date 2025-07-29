package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.AuthException;

public class UserNotAccessibleToHeroException extends AuthException {

  public UserNotAccessibleToHeroException() {
  }

  public UserNotAccessibleToHeroException(String msg) {
    super(msg);
  }
}
