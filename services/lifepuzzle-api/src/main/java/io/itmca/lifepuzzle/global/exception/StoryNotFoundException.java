package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;

public class StoryNotFoundException extends NotFoundException {

  private StoryNotFoundException(String msg) {
    super(msg);
  }

  public static StoryNotFoundException byStoryId(String storyId) {
    return new StoryNotFoundException(String.format("Story is not found - storyId: %s", storyId));
  }

  public static StoryNotFoundException byHeroNo(Long heroNo) {
    return new StoryNotFoundException(String.format("Story is not found - heroNo: %d", heroNo));
  }
}
