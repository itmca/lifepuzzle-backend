package io.itmca.lifepuzzle.domain.hero.endpoint;

import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("heroes")
public class HeroQueryEndpoint {

    HeroQueryService heroQueryService;

    @Autowired
    public HeroQueryEndpoint(HeroQueryService heroQueryService) {
        this.heroQueryService = heroQueryService;
    }

    @GetMapping("")
    public List<HeroQueryResponse> getHeroes(@RequestParam("user") Long userNo) {
        List<Hero> Heroes = heroQueryService.findHeroesByUserNo(userNo);
        List<HeroQueryResponse> heroQueryResponses = new ArrayList<>();

        for(Hero hero: Heroes){
            heroQueryResponses.add(HeroQueryResponse.from(hero));
        }

        return heroQueryResponses;
    }

    @GetMapping("/{heroNo}/{userNo}")
    public HeroQueryResponse getHeroDetail(@PathVariable("heroNo") Long heroNo, @PathVariable("userNo") Long userNo){
        Hero hero = heroQueryService.findByUserNoAndHeroNo(userNo, heroNo);
        HeroQueryResponse heroResponse = hero == null ? null : HeroQueryResponse.from(hero);

        return heroResponse;
    }

}
