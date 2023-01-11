package io.itmca.lifepuzzle.domain.register.service;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.hero.service.HeroWriteService;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterPostActionService {

    private final HeroWriteService heroWriteService;
    private final HeroUserAuthWriteService heroUserAuthWriteService;
    private final UserWriteService userWriteService;

    @Async
    public void doAfterRegisterActions(User user) {
        this.createHeroOfUser(user);
    }

    @Async
    private void createHeroOfUser(User user) {
        var hero = heroWriteService.create(Hero.defaultHero());

        heroUserAuthWriteService.create(
                HeroUserAuth.builder()
                        .userNo(user.getUserNo())
                        .hero(hero)
                        .build());

        // userWriteService.changeRecentHeroNo(user, hero.getHeroNo());

        user.changeRecentHeroNo(hero.getHeroNo());
        userWriteService.save(user);
    }
}