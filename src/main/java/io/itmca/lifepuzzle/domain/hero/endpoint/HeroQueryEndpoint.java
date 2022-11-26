package io.itmca.lifepuzzle.domain.hero.endpoint;

import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("heroes")
@RequiredArgsConstructor
public class HeroQueryEndpoint {

    private final HeroQueryService heroQueryService;

    @GetMapping("")
    public List<HeroQueryResponse> getHeroes(@RequestParam("user") Long userNo) {
        var heroes = heroQueryService.findHeroesByUserNo(userNo);

        return heroes.stream().map(HeroQueryResponse::from).toList();
    }

    @GetMapping("/{heroNo}/{userNo}")
    public HeroQueryResponse getHeroDetail(@PathVariable("heroNo") Long heroNo, @PathVariable("userNo") Long userNo){
        Hero hero = heroQueryService.findHeroByUserValidation(userNo, heroNo);

        return HeroQueryResponse.from(hero);
    }
}
