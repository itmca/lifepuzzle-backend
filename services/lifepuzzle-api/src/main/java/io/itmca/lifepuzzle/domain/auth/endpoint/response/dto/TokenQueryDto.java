package io.itmca.lifepuzzle.domain.auth.endpoint.response.dto;

import java.time.LocalDateTime;

public record TokenQueryDto(
    String accessToken,
    LocalDateTime accessTokenExpireAt,
    String refreshToken,
    LocalDateTime refreshTokenExpireAt,
    String socialToken
) {}
