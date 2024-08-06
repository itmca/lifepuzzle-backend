package io.itmca.lifepuzzle.domain.user.endpoint.request;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class UserUpdateRequest {
  private Long userNo;
  private String userNickName;
  private String imageURL;
}
