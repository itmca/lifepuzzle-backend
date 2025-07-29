package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.AuthException;

public class UserNotShareHeroAuthException extends AuthException {

  public UserNotShareHeroAuthException() {
  }

  public UserNotShareHeroAuthException(String msg) {
    super(msg);
  }
}
