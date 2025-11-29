package io.itmca.lifepuzzle.domain.user.endpoint.response;

import io.itmca.lifepuzzle.domain.user.endpoint.response.dto.UserQueryDto;
import io.itmca.lifepuzzle.domain.user.entity.User;

public record UserQueryResponse(UserQueryDto user) {

  public static UserQueryResponse from(User user) {
    return new UserQueryResponse(UserQueryDto.from(user));
  }
}
