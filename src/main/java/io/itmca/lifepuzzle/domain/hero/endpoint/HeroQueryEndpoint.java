package io.itmca.lifepuzzle.domain.hero.endpoint;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroListQueryResponse;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroQueryDTO;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.global.constant.ServerConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "주인공 조회")
public class HeroQueryEndpoint {
  private final HeroQueryService heroQueryService;
  private final HeroValidationService heroValidationService;
  private final UserQueryService userQueryService;

  @Deprecated
  @GetMapping("/heroes")
  public List<HeroQueryDTO> getHeroes(@AuthenticationPrincipal AuthPayload authPayload) {
    var heroes = heroQueryService.findHeroesByUserNo(authPayload.getUserNo());

    return heroes.stream().map(HeroQueryDTO::from).toList();
  }

  @Operation(summary = "주인공 전체 목록 조회")
  @GetMapping("/heroes/v2")
  public HeroListQueryResponse getHeroesV2(@AuthenticationPrincipal AuthPayload authPayload) {
    var user = userQueryService.findByUserNo(authPayload.getUserNo());

    return heroQueryService.toQueryResponses(user);
  }

  @Operation(summary = "주인공 조회")
  @GetMapping("/heroes/{heroNo}")
  public HeroQueryResponse getHeroDetail(
      @PathVariable("heroNo") @Schema(description = "주인공키") Long heroNo,
      @AuthenticationPrincipal AuthPayload authPayload) {
    heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);

    var hero = heroQueryService.findHeroByHeroNo(heroNo);

    return heroQueryService.toQueryResponse(hero);
  }

  @Operation(summary = "주인공 권한 링크 조회")
  @GetMapping("/heroes/auth/{heroNo}/link")
  public String getHeroAuthLink(@PathVariable("heroNo") Long heroNo,
                                @RequestParam("auth") HeroAuthStatus heroAuthStatus,
                                @AuthenticationPrincipal AuthPayload authPayload) {
    return ServerConstant.SERVER_HOST + "/" + heroNo + "/" + heroAuthStatus;
  }

}
