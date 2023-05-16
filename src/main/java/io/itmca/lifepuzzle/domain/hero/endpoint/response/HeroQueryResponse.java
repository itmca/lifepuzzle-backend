package io.itmca.lifepuzzle.domain.hero.endpoint.response;

import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroQueryDTO;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HeroQueryResponse {

  private HeroQueryDTO hero;

  public static HeroQueryResponse from(Hero hero) {
    return HeroQueryResponse.builder()
        .hero(HeroQueryDTO.from(hero))
        .build();
  }

}
