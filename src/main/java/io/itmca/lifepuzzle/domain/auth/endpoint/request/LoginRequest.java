package io.itmca.lifepuzzle.domain.auth.endpoint.request;

import lombok.Getter;

@Getter
public class LoginRequest {
  private String username;
  private String password;
}
