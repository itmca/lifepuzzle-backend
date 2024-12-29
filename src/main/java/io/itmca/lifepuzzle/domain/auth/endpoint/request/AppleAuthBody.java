package io.itmca.lifepuzzle.domain.auth.endpoint.request;

import lombok.Getter;

@Getter
public class AppleAuthBody {
  private String appleUserId;
  private String email;
  private String identityToken;
  private String nonce;
  private String shareKey;
}