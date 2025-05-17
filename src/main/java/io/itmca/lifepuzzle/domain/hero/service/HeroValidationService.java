package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.repository.HeroUserAuthRepository;
import io.itmca.lifepuzzle.global.exception.UserNotAccessibleToHeroException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
