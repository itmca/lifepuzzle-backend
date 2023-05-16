package io.itmca.lifepuzzle.domain.auth;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Token {
  private String accessToken;
  private LocalDateTime accessTokenExpireAt;
  private String refreshToken;
  private LocalDateTime refreshTokenExpireAt;
  private String socialToken;

  public void addSocialToken(String socialToken) {
    this.socialToken = socialToken;
  }
}
