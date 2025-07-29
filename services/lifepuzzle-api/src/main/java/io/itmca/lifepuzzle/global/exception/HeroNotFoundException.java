package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;

public class HeroNotFoundException extends NotFoundException {

  private HeroNotFoundException(String msg) {
    super(msg);
  }

  public static HeroNotFoundException byHeroNo(Long heroNo) {
    return new HeroNotFoundException(String.format("Hero is not found - heroNo: %d", heroNo));
  }

  public static HeroNotFoundException byUserNo(Long userNo) {
    return new HeroNotFoundException(String.format("Hero is not found - userNo: %d", userNo));
  }
}
