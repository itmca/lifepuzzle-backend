package io.itmca.lifepuzzle.domain.hero.endpoint;

import io.itmca.lifepuzzle.domain.auth.jwt.UserAuthentication;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.hero.repository.HeroQueryRepository;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

        return HeroQueryResponse.from(heroQueryService.findHeroByUserValidation(heroNo));
    }
}
