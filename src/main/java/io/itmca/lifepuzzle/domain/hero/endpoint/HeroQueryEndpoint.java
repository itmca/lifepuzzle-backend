package io.itmca.lifepuzzle.domain.hero.endpoint;

import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HeroQueryEndpoint {

    private final HeroQueryService heroQueryService;
    private final HeroValidationService heroValidationService;

    @GetMapping("/heroes")
    public List<HeroQueryResponse> getHeroes(@RequestParam("user") Long userNo) {
        var heroes = heroQueryService.findHeroesByUserNo(userNo);

        return heroes.stream().map(HeroQueryResponse::from).toList();
    }

    @GetMapping("/heroes/{heroNo}")
    public HeroQueryResponse getHeroDetail(@PathVariable("heroNo") Long heroNo, @PathVariable("userNo") Long userNo){
        heroValidationService.validateUserCanAccessHero(userNo, heroNo);

        return HeroQueryResponse.from(heroQueryService.findHeroByHeroNo(heroNo));
    }
}
