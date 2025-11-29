package io.itmca.lifepuzzle.domain.user.endpoint.request;

import io.itmca.lifepuzzle.domain.user.entity.User;
import java.time.LocalDate;

public record UserRegisterRequest(
    String id,
    String nickName,
    String email,
    String password,
    LocalDate birthday,
    String shareKey
) {
  public User toUser() {
    return User.builder()
        .loginId(id)
        .nickName(nickName)
        .email(email)
        .password(password)
        .birthday(birthday)
        .build();
  }
}
