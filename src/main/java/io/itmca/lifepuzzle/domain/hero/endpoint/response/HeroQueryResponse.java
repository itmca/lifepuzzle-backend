package io.itmca.lifepuzzle.domain.hero.endpoint.response;

import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroQueryDTO;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroUserAuthQueryDTO;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.Comparator;
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

  public static HeroQueryResponse from(Hero hero, Long userNo, int puzzleCnt) {
    var mainUserHeroAuthQueryDTO = hero.getHeroUserAuths().stream()
        .filter(heroUserAuth -> heroUserAuth.getUser().getId().equals(userNo))
        .map(HeroUserAuthQueryDTO::from)
        .toList();
    var otherUserHeroAuthQueryDTOs = hero.getHeroUserAuths().stream()
        .filter(heroUserAuth -> !heroUserAuth.getUser().getId().equals(userNo))
        .map(HeroUserAuthQueryDTO::from)
        .sorted(Comparator.comparing(HeroUserAuthQueryDTO::getNickName))
        .toList();

    var heroAuthQueryDTOs = new ArrayList<HeroUserAuthQueryDTO>();
    heroAuthQueryDTOs.addAll(mainUserHeroAuthQueryDTO);
    heroAuthQueryDTOs.addAll(otherUserHeroAuthQueryDTOs);

    return HeroQueryResponse.builder()
        .hero(HeroQueryDTO.from(hero, userNo))
        .users(heroAuthQueryDTOs)
        .puzzleCnt(puzzleCnt)
        .build();
  }
}
