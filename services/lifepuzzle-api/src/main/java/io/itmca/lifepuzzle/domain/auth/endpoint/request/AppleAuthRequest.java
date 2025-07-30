package io.itmca.lifepuzzle.domain.auth.endpoint.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AppleAuthRequest {
  private String appleUserId;
  private String email;
  private String identityToken;
  private String nonce;
  private String shareKey;
}