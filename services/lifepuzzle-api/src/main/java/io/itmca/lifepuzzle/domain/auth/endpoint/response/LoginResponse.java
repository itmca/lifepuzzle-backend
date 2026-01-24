package io.itmca.lifepuzzle.domain.auth.endpoint.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.dto.TokenQueryDto;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.user.endpoint.response.dto.UserQueryDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoginResponse(
    UserQueryDto user,
    TokenQueryDto tokens,
    HeroQueryResponse hero,
    Boolean isNewUser
) {}
