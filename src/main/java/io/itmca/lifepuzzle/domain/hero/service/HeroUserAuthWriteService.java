package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.repository.HeroUserAuthRepository;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.itmca.lifepuzzle.global.exception.UserNotAccessibleToHeroException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeroUserAuthWriteService {
  private final HeroUserAuthRepository heroUserAuthRepository;

  public HeroUserAuth create(HeroUserAuth heroUserAuth) {
    return heroUserAuthRepository.save(heroUserAuth);
  }

  @Transactional
  public HeroUserAuth update(long userNo, long heroNo, HeroAuthStatus heroAuthStatus) {
    HeroUserAuth heroUserAuth = heroUserAuthRepository.findByUserNoAndHeroNo(userNo, heroNo)
        .orElseThrow(UserNotAccessibleToHeroException::new);

    heroUserAuth.changeAuth(heroAuthStatus);
    return heroUserAuth;
  }

  public void remove(HeroUserAuth heroUserAuth) {
    heroUserAuthRepository.delete(heroUserAuth);
  }
}
