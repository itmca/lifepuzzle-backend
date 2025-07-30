package io.itmca.lifepuzzle.domain.auth.endpoint.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.itmca.lifepuzzle.global.util.MaskedSerializer;
import lombok.Getter;

@Getter
public class LoginRequest {
  private String username;
  @JsonSerialize(using = MaskedSerializer.class)
  private String password;
  private String shareKey;
}
