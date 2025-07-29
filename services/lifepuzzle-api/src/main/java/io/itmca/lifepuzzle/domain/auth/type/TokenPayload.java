package io.itmca.lifepuzzle.domain.auth.type;

public enum TokenPayload {
  UserNo("userNo"),
  Type("type"),
  IssueDate("iat"),
  ExpireDate("exp");

  private String key;

  TokenPayload(String key) {
    this.key = key;
  }

  public String key() {
    return this.key;
  }
}
