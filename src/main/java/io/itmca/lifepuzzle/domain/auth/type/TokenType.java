package io.itmca.lifepuzzle.domain.auth.type;

public enum TokenType {
  ACCESS("access"), REFRESH("refresh");

  private String frontEndKey;

  TokenType(String frontEndKey) {
    this.frontEndKey = frontEndKey;
  }

  public String frontEndKey() {
    return frontEndKey;
  }
}
