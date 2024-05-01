package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.MissingArgumentException;

public class MissingHeroNoException extends MissingArgumentException {
  public MissingHeroNoException() {
    super("heroNo is required in the request");
  }
}
