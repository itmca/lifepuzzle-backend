package io.itmca.lifepuzzle.domain.register.endpoint.request;

import lombok.Getter;

@Getter
public class UserWithdrawRequest {
  private String socialToken;
}