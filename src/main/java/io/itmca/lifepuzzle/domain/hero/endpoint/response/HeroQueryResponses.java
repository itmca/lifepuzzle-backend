package io.itmca.lifepuzzle.domain.hero.endpoint.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HeroQueryResponses {
  private List<HeroQueryResponse> heroes;
}
