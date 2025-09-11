package io.itmca.lifepuzzle.domain.auth.endpoint.response.dto;

import java.time.LocalDateTime;

public record TokenQueryDto(
    String accessToken,
    // TODO(joo-on): FE에서 만료 시간을 토큰 파싱해서 확인하도록 변경하였고 FE배포 이후 불필요한 만료시간 필드 제거
    @Deprecated(forRemoval = true)
    LocalDateTime accessTokenExpireAt,
    String refreshToken,
    @Deprecated(forRemoval = true)
    LocalDateTime refreshTokenExpireAt,
    String socialToken
) {}
