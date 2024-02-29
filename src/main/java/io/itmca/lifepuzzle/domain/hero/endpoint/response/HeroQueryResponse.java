package io.itmca.lifepuzzle.domain.hero.endpoint.response;

import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroQueryDTO;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroUserAuthQueryDTO;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HeroQueryResponse {
  private HeroQueryDTO hero;
  private List<HeroUserAuthQueryDTO> users;
  @Schema(description = "맞춘 퍼즐 개수")
  private int puzzleCnt;

  public static HeroQueryResponse from(List<HeroUserAuth> heroUserAuths, Hero hero, int puzzleCnt) {
    var heroUserAuthQueryDTOs = heroUserAuths.stream()
        .map(HeroUserAuthQueryDTO::from)
        .toList();

    return HeroQueryResponse.builder()
        .hero(HeroQueryDTO.from(hero))
        .users(heroUserAuthQueryDTOs)
        .puzzleCnt(puzzleCnt)
        .build();
  }
}
