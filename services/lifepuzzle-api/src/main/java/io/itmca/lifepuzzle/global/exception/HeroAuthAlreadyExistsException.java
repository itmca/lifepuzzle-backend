package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.AlreadyExistsException;

public class HeroAuthAlreadyExistsException extends AlreadyExistsException {
  public HeroAuthAlreadyExistsException(String msg) {
    super(msg);
  }
}
