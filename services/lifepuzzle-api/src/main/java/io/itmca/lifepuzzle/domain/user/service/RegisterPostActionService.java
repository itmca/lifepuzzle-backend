package io.itmca.lifepuzzle.domain.user.service;

import ch.qos.logback.core.util.StringUtil;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterPostActionService {

  private final HeroUserAuthWriteService heroUserAuthWriteService;
  private final UserWriteService userWriteService;

  public void doAfterRegisterActions(User user, String shareKey) {
    // Only create hero connection when shareKey is provided
    if (!StringUtil.isNullOrEmpty(shareKey)) {
      var newHeroUserAuth = heroUserAuthWriteService.createByShareKey(user, shareKey);
      Hero hero = newHeroUserAuth.getHero();
      userWriteService.changeRecentHero(user, hero.getHeroNo());
    }
    // If no shareKey, user will create their own hero from frontend
  }
}
