package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;

public class StoryNotFoundException extends NotFoundException {

  private StoryNotFoundException(String msg) {
    super(msg);
  }

  public static StoryNotFoundException byStoryKey(String storyKey) {
    return new StoryNotFoundException(String.format("Story is not found - storyKey: %s", storyKey));
  }

  public static StoryNotFoundException byHeroNo(Long heroNo) {
    return new StoryNotFoundException(String.format("Story is not found - heroNo: %d", heroNo));
  }
}
