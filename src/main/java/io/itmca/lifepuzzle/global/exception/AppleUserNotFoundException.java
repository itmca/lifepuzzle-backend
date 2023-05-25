package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;

public class AppleUserNotFoundException extends NotFoundException {
  public AppleUserNotFoundException(String appleId) {
    super(String.format("User is not found - appleId: %s", appleId));
  }
}
