package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;

public class UserHeroShareKeyNotFoundException extends NotFoundException {
  
  public UserHeroShareKeyNotFoundException(String msg) {
    super(msg);
  }
}
