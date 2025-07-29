package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.AuthException;

public class UserNoMismatchException extends AuthException {
  public UserNoMismatchException() {
    super("userNo does not match");
  }
}
