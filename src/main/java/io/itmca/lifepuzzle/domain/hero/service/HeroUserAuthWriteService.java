package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.repository.HeroUserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeroUserAuthWriteService {
  private final HeroUserAuthRepository heroUserAuthRepository;

  public HeroUserAuth create(HeroUserAuth heroUserAuth) {
    return heroUserAuthRepository.save(heroUserAuth);
  }

  public HeroUserAuth update(HeroUserAuth heroUserAuth) {
    return heroUserAuthRepository.save(heroUserAuth);
  }

  public void remove(HeroUserAuth heroUserAuth) {
    heroUserAuthRepository.delete(heroUserAuth);
  }
}
