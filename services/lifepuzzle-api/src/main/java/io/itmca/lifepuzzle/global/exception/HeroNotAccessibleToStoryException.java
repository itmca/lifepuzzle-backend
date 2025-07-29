package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.AuthException;

public class HeroNotAccessibleToStoryException extends AuthException {
  public HeroNotAccessibleToStoryException(Long heroNo, String storyKey) {
    super(String.format("Hero: %d is not accessible to story - storyKey: %s", heroNo, storyKey));
  }
}
