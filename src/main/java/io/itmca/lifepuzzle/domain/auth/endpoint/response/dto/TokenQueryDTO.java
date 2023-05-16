package io.itmca.lifepuzzle.domain.auth.endpoint.response.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TokenQueryDTO {
    private String accessToken;
    private LocalDateTime accessTokenExpireAt;
    private String refreshToken;
    private LocalDateTime refreshTokenExpireAt;
    private String socialToken;
}
