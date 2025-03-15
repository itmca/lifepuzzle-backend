package io.itmca.lifepuzzle.domain.auth.endpoint.response.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserQueryDto {
  private Long userNo;
  private String userNickName;
  private String userType;
}
