package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.repository.HeroRepository;
import io.itmca.lifepuzzle.domain.hero.repository.HeroUserAuthRepository;
import io.itmca.lifepuzzle.global.exception.HeroNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeroQueryService {
    private final HeroRepository heroRepository;
    private final HeroUserAuthRepository heroUserAuthRepository;

    public Hero findHeroByHeroNo(Long heroNo) {
         return this.heroRepository.findById(heroNo).orElseThrow(() -> new HeroNotFoundException(heroNo));
    }

    public List<Hero> findHeroesByUserNo(Long userNo) {
        var heroUserAuths = this.heroUserAuthRepository.findAllByUserNo(userNo);

        return heroUserAuths.stream()
                .map(HeroUserAuth::getHero)
                .toList();
    }
}
