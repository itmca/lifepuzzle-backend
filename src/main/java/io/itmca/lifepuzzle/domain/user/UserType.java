package io.itmca.lifepuzzle.domain.user;

public enum UserType {
  APPLE("apple"), KAKAO("kakao"), GENERAL("general");

  private String frontEndKey;

  UserType(String frontEndKey) {
    this.frontEndKey = frontEndKey;
  }

  public String frontEndKey() {
    return frontEndKey;
  }
}
