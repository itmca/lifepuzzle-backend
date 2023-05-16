package io.itmca.lifepuzzle.domain.hero.endpoint;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroQueryDTO;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "주인공 조회")
public class HeroQueryEndpoint {

  private final HeroQueryService heroQueryService;
  private final HeroValidationService heroValidationService;

  @Operation(summary = "주인공 전체 목록 조회")
  @GetMapping("/heroes")
  public List<HeroQueryDTO> getHeroes(@AuthenticationPrincipal AuthPayload authPayload) {
    var heroes = heroQueryService.findHeroesByUserNo(authPayload.getUserNo());

    return heroes.stream().map(HeroQueryDTO::from).toList();
  }

  @Operation(summary = "주인공 조회")
  @GetMapping("/heroes/{heroNo}")
  public HeroQueryDTO getHeroDetail(@PathVariable("heroNo") Long heroNo,
                                    @AuthenticationPrincipal AuthPayload authPayload) {
    heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);

    return HeroQueryDTO.from(heroQueryService.findHeroByHeroNo(heroNo));
  }
}
