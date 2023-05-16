package io.itmca.lifepuzzle.domain.register.endpoint.request;

import io.itmca.lifepuzzle.domain.user.entity.User;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class UserRegisterRequest {
  private String id;
  private String nickName;
  private String email;
  private String password;
  private LocalDate birthday;

  public User toUser() {
    return User.builder()
        .userId(id)
        .nickName(nickName)
        .email(email)
        .password(password)
        .birthday(birthday)
        .build();
  }
}
