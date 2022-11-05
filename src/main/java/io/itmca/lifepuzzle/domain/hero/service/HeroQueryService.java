package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.repository.HeroQueryRepository;
import io.itmca.lifepuzzle.global.type.Hero;

public class HeroQueryService {
    private final HeroQueryRepository heroQueryRepository;

    public HeroQueryService(HeroQueryRepository heroQueryRepository) {
        this.heroQueryRepository = heroQueryRepository;
    }

    public Hero findByHeroNo(int herNo) {}
    public Hero findHeroesByUserNo(int userNo) {}
}
