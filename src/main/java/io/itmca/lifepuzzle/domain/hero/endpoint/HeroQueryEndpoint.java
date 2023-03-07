package io.itmca.lifepuzzle.domain.hero.endpoint;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryDTO;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HeroQueryEndpoint {

    private final HeroQueryService heroQueryService;
    private final HeroValidationService heroValidationService;

    @GetMapping("/heroes")
    public List<HeroQueryDTO> getHeroes(@AuthenticationPrincipal AuthPayload authPayload) {
        var heroes = heroQueryService.findHeroesByUserNo(authPayload.getUserNo());

        return heroes.stream().map(HeroQueryDTO::from).toList();
    }

    @GetMapping("/heroes/{heroNo}")
    public HeroQueryDTO getHeroDetail(@PathVariable("heroNo") Long heroNo,
                                           @AuthenticationPrincipal AuthPayload authPayload){
        heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);

        return HeroQueryDTO.from(heroQueryService.findHeroByHeroNo(heroNo));
    }
}
