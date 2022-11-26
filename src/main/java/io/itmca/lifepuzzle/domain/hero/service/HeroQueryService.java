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

    /**
    * @throws UserNotAccessibleToHeroException 유저가 권한이 있는 주인공이 없는 경우
    * */
    public Hero findHeroByUserValidation(Long userNo, Long heroNo) {
        var findHero = Hero.builder()
                .heroNo(heroNo)
                .build();
        var optionalHeroUserAuth = this.heroQueryRepository.findByUserNoAndHero(userNo, findHero);
        var heroUserAuth = optionalHeroUserAuth.orElseThrow(() -> new UserNotAccessibleToHeroException());

        return heroUserAuth.getHero();
    }

    /*
    * 권한 체크에 대해서 서비스를 따로 분리해야할지 고민?
    * boolean 현태로 넘길지도 고민? 아니면 예외 처리 할지 고민?
     */
    public HeroUserAuth findHeroUserAuth(Long userNo, Long heroNo){
        var findHero = Hero.builder()
                .heroNo(heroNo)
                .build();
        var optionalHeroUserAuth = this.heroQueryRepository.findByUserNoAndHero(userNo, findHero);
        var heroUserAuth = optionalHeroUserAuth.orElseThrow(() -> new UserNotAccessibleToHeroException());

        return heroUserAuth;
    }
    public List<Hero> findHeroesByUserNo(Long userNo) {
        var heroUserAuths = this.heroQueryRepository.findAllByUserNo(userNo);

        return Arrays.stream(heroUserAuths)
                .map(HeroUserAuth::getHero)
                .toList();
    }
}
