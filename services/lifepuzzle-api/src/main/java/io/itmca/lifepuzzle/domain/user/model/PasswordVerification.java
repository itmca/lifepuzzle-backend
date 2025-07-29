package io.itmca.lifepuzzle.domain.user.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordVerification {
  private String plainPassword;
  private String salt;
  private String hashedPassword;
}
