package io.itmca.lifepuzzle.domain.auth.endpoint.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.dto.TokenQueryDTO;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.dto.UserQueryDTO;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroQueryDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private UserQueryDTO user;
    private TokenQueryDTO tokens;
    private HeroQueryDTO hero;
    private Boolean isNewUser;
}
