package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.repository.HeroRepository;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import io.itmca.lifepuzzle.global.infra.file.repository.S3Repository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeroWriteService {
  private final HeroRepository heroRepository;
  private final S3Repository s3Repository;

  public Hero create(Hero hero) {
    return heroRepository.save(hero);
  }

  public Hero update(Hero hero) {
    return heroRepository.save(hero);
  }

  public void remove(Long heroNo) {
    heroRepository.deleteById(heroNo);
  }

  public void saveHeroProfile(CustomFile customFile) throws IOException {
    s3Repository.upload(customFile);
  }
}
