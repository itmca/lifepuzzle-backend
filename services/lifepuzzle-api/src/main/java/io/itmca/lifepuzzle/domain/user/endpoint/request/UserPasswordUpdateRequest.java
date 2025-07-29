package io.itmca.lifepuzzle.domain.user.endpoint.request;

import lombok.Getter;

@Getter
public class UserPasswordUpdateRequest {
  private String oldPassword;
  private String newPassword;
}
