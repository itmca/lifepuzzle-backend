package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.AuthException;

public class UserNotAccessibleToStoryException extends AuthException {
  public UserNotAccessibleToStoryException(Long userNo, String storyKey) {
    super(String.format("User: %d is not accessible to story - storyKey: %s", userNo, storyKey));
  }
}
