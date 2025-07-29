package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.ExpiredException;

public class UserHeroShareExpiredDateException extends ExpiredException {

  public UserHeroShareExpiredDateException(String msg) {
    super(msg);
  }
}
