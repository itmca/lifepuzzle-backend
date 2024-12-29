package io.itmca.lifepuzzle.domain.register.service;

import ch.qos.logback.core.util.StringUtil;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.hero.service.HeroWriteService;
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

  public void doAfterRegisterActions(User user, String shareKey) {
    heroUserAuthWriteService.createIfShareKeyPresent(user, shareKey);

    if (StringUtil.isNullOrEmpty(shareKey)) {
      this.createHeroOfUser(user);
    }
  }

  private void createHeroOfUser(User user) {
    var hero = heroWriteService.createDefaultHero(user);

    userWriteService.changeRecentHeroNo(user, hero.getHeroNo());
  }
}
