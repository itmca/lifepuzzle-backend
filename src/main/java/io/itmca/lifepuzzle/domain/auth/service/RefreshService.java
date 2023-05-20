package io.itmca.lifepuzzle.domain.auth.service;

import static io.itmca.lifepuzzle.domain.auth.jwt.JwtTokenProvider.parseTokenType;
import static io.itmca.lifepuzzle.domain.auth.jwt.JwtTokenProvider.parseUserNo;

import io.itmca.lifepuzzle.domain.auth.Token;
import io.itmca.lifepuzzle.domain.auth.TokenType;
import io.itmca.lifepuzzle.global.exception.TokenTypeMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshService {

  private final TokenIssueService tokenIssueService;

  public Token refresh(String refreshToken) {
    if (!isRefreshToken(refreshToken)) {
      throw TokenTypeMismatchException.refreshTokenExpected(refreshToken);
    }

    return tokenIssueService.getTokensOfUser(parseUserNo(refreshToken));
  }

  private boolean isRefreshToken(String refreshToken) {
    return TokenType.REFRESH.frontEndKey().equals(parseTokenType(refreshToken));
  }
}
