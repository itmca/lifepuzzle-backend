package io.itmca.lifepuzzle.domain.hero.file;

import static io.itmca.lifepuzzle.global.constant.FileConstant.HERO_PROFILE_BASE_PATH;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import java.io.File;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class HeroProfileImage extends CustomFile {
  public HeroProfileImage(Hero hero, MultipartFile file) {
    super(
        String.join(File.separator, HERO_PROFILE_BASE_PATH, hero.getHeroNo().toString(), "image"),
        file);
  }
}
