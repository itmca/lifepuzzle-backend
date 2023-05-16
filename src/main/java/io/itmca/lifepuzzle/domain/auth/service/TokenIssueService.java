package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.Token;
import io.itmca.lifepuzzle.domain.auth.TokenType;
import io.itmca.lifepuzzle.domain.auth.jwt.JwtTokenProvider;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TokenIssueService {

  private static final int ACCESS_TOKEN_DURATION_SECONDS = 60 * 30;
  private static final int REFRESH_TOKEN_DURATION_SECONDS = 60 * 60 * 24 * 14;

  public Token getTokensOfUser(Long userNo) {
    var now = Instant.now();
    var expiryDateOfAccessToken = now.plusSeconds(ACCESS_TOKEN_DURATION_SECONDS);
    var expiryDateOfRefreshToken = now.plusSeconds(REFRESH_TOKEN_DURATION_SECONDS);

    var accessTokenPayload = Map.of(
        "userNo", userNo,
        "type", TokenType.ACCESS.frontEndKey(),
        "iat", now.getEpochSecond(),
        "exp", expiryDateOfAccessToken.getEpochSecond()
    );
    var accessToken = JwtTokenProvider.generateToken(accessTokenPayload);

    var refreshTokenPayload = Map.of(
        "userNo", userNo,
        "type", TokenType.REFRESH.frontEndKey(),
        "iat", now.getEpochSecond(),
        "exp", expiryDateOfRefreshToken.getEpochSecond()
    );
    var refreshToken = JwtTokenProvider.generateToken(refreshTokenPayload);

    return Token.builder()
        .accessToken(accessToken)
        .accessTokenExpireAt(
            LocalDateTime.ofInstant(expiryDateOfAccessToken, ZoneId.of("Asia/Seoul")))
        .refreshToken(refreshToken)
        .refreshTokenExpireAt(
            LocalDateTime.ofInstant(expiryDateOfRefreshToken, ZoneId.of("Asia/Seoul")))
        .build();
  }
}
