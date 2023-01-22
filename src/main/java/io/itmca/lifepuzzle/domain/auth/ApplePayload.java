package io.itmca.lifepuzzle.domain.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplePayload {
    private String iss;
    private long iat;
    private long exp;
    private String aud;
    private String sub;
}