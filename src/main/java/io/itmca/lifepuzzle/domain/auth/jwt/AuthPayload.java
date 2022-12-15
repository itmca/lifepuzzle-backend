package io.itmca.lifepuzzle.domain.auth.jwt;

import lombok.Getter;

@Getter
public class AuthPayload {

    private Long userNo;

    public AuthPayload(Long userNo) {
        this.userNo = userNo;
    }
}
