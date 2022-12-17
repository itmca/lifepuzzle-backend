package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.Token;
import io.itmca.lifepuzzle.domain.auth.jwt.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class TokenIssueService {

    public Token getTokensOfUser(Long userNo) {
        return JwtTokenProvider.generateToken(userNo);
    }
}
