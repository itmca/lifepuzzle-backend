package io.itmca.lifepuzzle.domain.hero.endpoint.response;

import java.util.List;

@Deprecated
public record HeroLegacyListQueryResponse(List<HeroLegacyQueryResponse> heroes) {}
