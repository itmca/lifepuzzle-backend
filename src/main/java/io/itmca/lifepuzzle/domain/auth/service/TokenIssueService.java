package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.entity.Token;
import io.itmca.lifepuzzle.domain.auth.jwt.JwtTokenProvider;
import io.itmca.lifepuzzle.domain.auth.jwt.UserAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenIssueService {

    public Token getTokensOfUser(String userId) {
        Authentication authentication = new UserAuthentication(userId, null, null);
        return Token.builder()
                .accessToken(JwtTokenProvider.generateToken(authentication))
                .accessTokenExpireAt(LocalDateTime.now()
                        .plusSeconds(JwtTokenProvider.ACCESS_TOKEN_DURATION))
                .refreshToken("")
                .refreshTokenExpireAt(LocalDateTime.now()
                        .plusSeconds(JwtTokenProvider.REFRESH_TOKEN_DURATION))
                .build();
    }
}
