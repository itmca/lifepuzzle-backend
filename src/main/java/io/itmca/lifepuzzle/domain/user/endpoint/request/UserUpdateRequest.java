package io.itmca.lifepuzzle.domain.user.endpoint.request;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class UserUpdateRequest {
  private String email;
  private String name;
  private LocalDate birthday;
  private String nickName;
}
