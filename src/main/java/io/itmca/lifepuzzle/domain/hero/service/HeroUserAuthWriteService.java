package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.endpoint.request.HeroChangeAuthRequest;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.repository.HeroUserAuthRepository;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.entity.UserHeroShare;
import io.itmca.lifepuzzle.domain.user.repository.UserHeroShareRepository;
import io.itmca.lifepuzzle.global.exception.HeroAuthAlreadyExistsException;
import io.itmca.lifepuzzle.global.exception.UserHeroShareExpiredDateException;
import io.itmca.lifepuzzle.global.exception.UserHeroShareKeyNotFoundException;
import io.itmca.lifepuzzle.global.exception.UserNotAccessibleToHeroException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeroUserAuthWriteService {
  private final HeroUserAuthRepository heroUserAuthRepository;
  private final UserHeroShareRepository userHeroShareRepository;

  public HeroUserAuth authorize(User user, Hero hero, HeroAuthStatus heroAuthStatus) {
    var heroUserAuth = HeroUserAuth.builder()
        .user(user)
        .hero(hero)
        .auth(heroAuthStatus)
        .build();

    return heroUserAuthRepository.save(heroUserAuth);
  }

  public HeroUserAuth createByShareKey(User user, String shareKey) {
    UserHeroShare userHeroShare = userHeroShareRepository
        .findById(shareKey)
        .orElseThrow(() -> new UserHeroShareKeyNotFoundException("권한 공유 정보가 존재하지 않습니다"));

    boolean isExpiredDate = LocalDateTime.now().isAfter(userHeroShare.getExpiredAt());

    if (isExpiredDate) {
      throw new UserHeroShareExpiredDateException("기간 만료 링크입니다");
    }

    boolean isExistHeroAuth = user.getHeroUserAuths() != null
        && user.getHeroUserAuths().stream()
        .anyMatch(h -> h.getHero().getHeroNo() == userHeroShare.getHeroId());

    if (isExistHeroAuth) {
      throw new HeroAuthAlreadyExistsException("이미 등록되어 있는 주인공입니다.");
    }

    return heroUserAuthRepository.save(
        HeroUserAuth
            .builder()
            .user(user)
            .hero(Hero
                .builder()
                .heroNo(userHeroShare.getHeroId())
                .build()
            )
            .auth(userHeroShare.getAuth())
            .build()
    );
  }

  @Transactional
  public void update(HeroChangeAuthRequest heroChangeAuthRequest) {
    HeroUserAuth heroUserAuth = heroUserAuthRepository
        .findByUserNoAndHeroNo(heroChangeAuthRequest.userNo(), heroChangeAuthRequest.heroNo())
        .orElseThrow(UserNotAccessibleToHeroException::new);

    heroUserAuth.changeAuth(heroChangeAuthRequest.heroAuthStatus());
  }

  public void remove(HeroUserAuth heroUserAuth) {
    heroUserAuthRepository.delete(heroUserAuth);
  }
}
