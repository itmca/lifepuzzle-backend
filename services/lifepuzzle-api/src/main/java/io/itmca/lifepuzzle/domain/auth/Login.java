package io.itmca.lifepuzzle.domain.auth;

import io.itmca.lifepuzzle.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Login {
  private User user;
  private String socialToken;
  private Boolean isNewUser;
}
