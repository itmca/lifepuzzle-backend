package io.itmca.lifepuzzle.domain.user.endpoint.request;

import lombok.Getter;

@Getter
public class UserWithdrawRequest {
  private String socialToken;
}