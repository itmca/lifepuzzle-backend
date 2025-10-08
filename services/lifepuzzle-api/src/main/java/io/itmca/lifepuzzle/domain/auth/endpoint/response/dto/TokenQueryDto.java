package io.itmca.lifepuzzle.domain.auth.endpoint.response.dto;

import java.time.LocalDateTime;

public record TokenQueryDto(
    String accessToken,
    String refreshToken,
    String socialToken
) {}
