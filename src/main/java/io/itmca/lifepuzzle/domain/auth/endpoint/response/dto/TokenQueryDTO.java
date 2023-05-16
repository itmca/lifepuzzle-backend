package io.itmca.lifepuzzle.domain.auth.endpoint.response.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenQueryDTO {
  private String accessToken;
  private LocalDateTime accessTokenExpireAt;
  private String refreshToken;
  private LocalDateTime refreshTokenExpireAt;
  private String socialToken;
}
