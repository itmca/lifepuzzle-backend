package io.itmca.lifepuzzle.domain.hero.endpoint.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record HeroChangeStatusRequest(
    @JsonAlias("userNo")
    @Schema(description = "권한 변경 대상 유저의 식별자")
    Long userId,
    @Schema(description = "변경하고자 하는 권한")
    HeroAuthStatus heroAuthStatus) {
}
