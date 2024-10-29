package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;

public class LikeNotFoundException extends NotFoundException {
  public LikeNotFoundException(String storyId) {
    super(String.format("Like is not found - storyId: %s", storyId));
  }
}
