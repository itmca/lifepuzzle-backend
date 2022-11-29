package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.repository.HeroQueryRepository;
import io.itmca.lifepuzzle.global.exception.UserNotAccessibleToHeroException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class HeroQueryService {
    private final HeroQueryRepository heroQueryRepository;

    @Autowired
    public HeroQueryService(HeroQueryRepository heroQueryRepository) {
        this.heroQueryRepository = heroQueryRepository;
    }

    public Hero findHeroByUserValidation(Long heroNo) {
         return this.heroQueryRepository.findByHeroNoWithJPQL(heroNo).get();
    }

    public List<Hero> findHeroesByUserNo(Long userNo) {
        var heroUserAuths = this.heroQueryRepository.findAllByUserNo(userNo);

        return heroUserAuths.stream()
                .map(HeroUserAuth::getHero)
                .toList();
    }
}
