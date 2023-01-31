package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.Token;
import io.itmca.lifepuzzle.domain.auth.TokenType;
import io.itmca.lifepuzzle.domain.auth.jwt.JwtTokenProvider;
import io.itmca.lifepuzzle.global.exception.TokenTypeMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshService {

    private final TokenIssueService tokenIssueService;

    public Token refresh(String refreshToken) {
        if (!JwtTokenProvider.parseTokenType(refreshToken)
                .equals(TokenType.REFRESH.frontEndKey())) {
            throw new TokenTypeMismatchException("토큰 타입이 일치하지 않습니다.");
        }

        return tokenIssueService.getTokensOfUser(JwtTokenProvider.parseUserNo(refreshToken));
    }
}
