package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.AlreadyExistsException;

public class AlreadyLikedException extends AlreadyExistsException {
  public AlreadyLikedException(String msg) {
    super(msg);
  }

  public static AlreadyLikedException forStory(String storyId) {
    return new AlreadyLikedException(String.format("You have already liked this story - storyId: %s", storyId));
  }
}
