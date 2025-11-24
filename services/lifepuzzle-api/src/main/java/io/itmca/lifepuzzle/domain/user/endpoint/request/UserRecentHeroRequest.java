package io.itmca.lifepuzzle.domain.user.endpoint.request;

import com.fasterxml.jackson.annotation.JsonAlias;

public record UserRecentHeroRequest(
    @JsonAlias("heroNo")
    Long heroId
) {
}
