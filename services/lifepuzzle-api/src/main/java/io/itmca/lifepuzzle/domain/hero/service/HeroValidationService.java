package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.repository.HeroUserAuthRepository;
import io.itmca.lifepuzzle.global.exception.UserNotAccessibleToHeroException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// TODO(border-line): AuthCheckerAspect에서 권한 체크가 이루어지고 있어서 필요 없어진 클래스로 해아 클래스 및 사용처 제거 필요
@Service
@RequiredArgsConstructor
public class HeroValidationService {
  private final HeroUserAuthRepository heroUserAuthRepository;
  private final HeroQueryService heroQueryService;

  public void validateUserCanAccessHero(Long userNo, Long heroNo) {
    heroQueryService.validateHeroExistOrThrow(heroNo);

    Optional<HeroUserAuth> heroUserAuths =
        heroUserAuthRepository.findByUserNoAndHeroNo(userNo, heroNo);

    if (heroUserAuths.isEmpty()) {
      throw new UserNotAccessibleToHeroException();
    }
  }
}
