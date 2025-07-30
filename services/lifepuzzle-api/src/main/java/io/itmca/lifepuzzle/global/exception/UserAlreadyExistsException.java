package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.AlreadyExistsException;

public class UserAlreadyExistsException extends AlreadyExistsException {
  public UserAlreadyExistsException(String userId) {
    super(String.format("User already exists - userId: %s", userId));
  }
}
