package io.itmca.lifepuzzle.domain.user.endpoint.response;

import lombok.Builder;

@Builder
public record UserHeroShareResponse(
    String link
) {
}
