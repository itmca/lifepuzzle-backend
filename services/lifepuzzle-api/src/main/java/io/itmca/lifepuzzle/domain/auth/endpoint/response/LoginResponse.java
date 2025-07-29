package io.itmca.lifepuzzle.domain.auth.endpoint.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.dto.TokenQueryDto;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.dto.UserQueryDto;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroQueryDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
  private UserQueryDto user;
  private TokenQueryDto tokens;
  private HeroQueryDto hero;
  private Boolean isNewUser;
}
