package io.itmca.lifepuzzle.domain.register.service;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.hero.service.HeroWriteService;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterPostActionService {

  private final HeroWriteService heroWriteService;
  private final HeroUserAuthWriteService heroUserAuthWriteService;
  private final UserWriteService userWriteService;

  public void doAfterRegisterActions(User user) {
    this.createHeroOfUser(user);
  }

  private void createHeroOfUser(User user) {
    var hero = heroWriteService.create(Hero.defaultHero());

    heroUserAuthWriteService.create(
        HeroUserAuth.builder()
            .user(user)
            .hero(hero)
            .auth(HeroAuthStatus.OWNER)
            .build());

    userWriteService.changeRecentHeroNo(user, hero.getHeroNo());
  }
}
