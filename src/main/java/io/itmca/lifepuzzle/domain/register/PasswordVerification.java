package io.itmca.lifepuzzle.domain.register;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordVerification {
    private String plainPassword;
    private String salt;
    private String hashedPassword;
}
