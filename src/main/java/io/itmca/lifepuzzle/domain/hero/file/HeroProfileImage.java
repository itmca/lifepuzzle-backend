package io.itmca.lifepuzzle.domain.hero.file;

import static io.itmca.lifepuzzle.global.constant.FileConstant.HERO_PROFILE_BASE_PATH;
import static io.itmca.lifepuzzle.global.util.FileUtil.addRandomValueFilePrefix;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import java.io.File;
import java.io.IOException;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class HeroProfileImage implements CustomFile {
  private final String base;
  private final String fileName;
  private final byte[] bytes;

  public HeroProfileImage(Hero hero, MultipartFile multipartFile) {
    this.fileName = addRandomValueFilePrefix(multipartFile);
    this.base =
        String.join(File.separator, HERO_PROFILE_BASE_PATH, hero.getHeroNo().toString(), "image");

    try {
      this.bytes = multipartFile.getBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public CustomFile resize() {
    return null;
  }
}
