package io.itmca.lifepuzzle.domain.auth.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Token {
    private String accessToken;
    private LocalDateTime accessTokenExpireAt;
    private String refreshToken;
    private LocalDateTime refreshTokenExpireAt;
}
