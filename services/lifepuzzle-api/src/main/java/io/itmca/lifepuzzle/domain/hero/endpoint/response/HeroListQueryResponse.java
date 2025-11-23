package io.itmca.lifepuzzle.domain.hero.endpoint.response;

import java.util.List;

public record HeroListQueryResponse(List<HeroQueryResponse> heroes) {}
