package io.itmca.lifepuzzle.domain.hero.file;

import static io.itmca.lifepuzzle.global.constant.FileConstant.HERO_PROFILE_IMAGE_BASE_PATH_FORMAT;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class HeroProfileImage extends CustomFile {
  public HeroProfileImage(Hero hero, MultipartFile file) {
    super(
        HERO_PROFILE_IMAGE_BASE_PATH_FORMAT.formatted(hero.getHeroNo().toString()),
        file);
  }
}
