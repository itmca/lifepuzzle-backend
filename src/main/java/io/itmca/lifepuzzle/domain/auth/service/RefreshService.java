package io.itmca.lifepuzzle.domain.auth.service;

import static io.itmca.lifepuzzle.domain.auth.jwt.JwtTokenProvider.findTokenType;
import static io.itmca.lifepuzzle.domain.auth.jwt.JwtTokenProvider.toClaims;

import io.itmca.lifepuzzle.domain.auth.Token;
import io.itmca.lifepuzzle.domain.auth.jwt.JwtTokenProvider;
import io.itmca.lifepuzzle.domain.auth.type.TokenType;
import io.itmca.lifepuzzle.global.exception.RefreshTokenRequiredException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshService {

  private final TokenIssueService tokenIssueService;

  public Token refresh(String refreshToken) {
    var claims = toClaims(refreshToken).orElse(null);

    if (claims == null || !isRefreshToken(claims)) {
      throw new RefreshTokenRequiredException(refreshToken);
    }

    return tokenIssueService.getTokensOfUser(JwtTokenProvider.findUserNo(claims));
  }

  private boolean isRefreshToken(Claims claims) {
    var tokenType = findTokenType(claims);

    return TokenType.REFRESH.frontEndKey().equals(tokenType);
  }
}
