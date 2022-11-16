package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.repository.HeroQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HeroQueryService {
    private final HeroQueryRepository heroQueryRepository;

    @Autowired
    public HeroQueryService(HeroQueryRepository heroQueryRepository) {
        this.heroQueryRepository = heroQueryRepository;
    }

    public Hero findByUserNoAndHeroNo(Long userNo, Long heroNo) {
        Hero findHero = Hero.getHeroInstance();
        findHero.setHeroId(heroNo);
        Optional<HeroUserAuth> heroUserAuth = this.heroQueryRepository.findByUserNoAndHero(userNo, findHero);

        if(heroUserAuth.equals(Optional.empty())){
            System.out.println("EMPTYH OOHOHO");
            return null;
        }

        Hero hero = heroUserAuth.get().getHero();
        return hero;
    }
    public List<Hero> findHeroesByUserNo(Long userNo) {
        HeroUserAuth[] heroUserAuths = this.heroQueryRepository.findAllByUserNo(userNo);
        List<Hero> heroes = new ArrayList<>();

        for(HeroUserAuth heroUserAuth : heroUserAuths) {
            Hero hero = heroUserAuth.getHero();
            heroes.add(hero);
        }

        return heroes;
    }
}
