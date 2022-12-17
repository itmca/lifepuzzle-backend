package io.itmca.lifepuzzle.domain.user.endpoint.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserUpdateRequest {
    private String email;
    private String name;
    private LocalDate birthday;
    private String nickName;
}
