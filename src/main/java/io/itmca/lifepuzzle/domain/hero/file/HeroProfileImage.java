package io.itmca.lifepuzzle.domain.hero.file;

import static io.itmca.lifepuzzle.global.constant.FileConstant.HERO_IMAGE_RESIZING_HEIGHT;
import static io.itmca.lifepuzzle.global.constant.FileConstant.HERO_IMAGE_RESIZING_WIDTH;
import static io.itmca.lifepuzzle.global.constant.FileConstant.HERO_PROFILE_IMAGE_BASE_PATH_FORMAT;

import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.ImageFormats;
import io.github.techgnious.exception.ImageException;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.global.infra.file.CustomFile;
import io.itmca.lifepuzzle.global.infra.file.Resizable;
import java.io.File;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Slf4j
public class HeroProfileImage extends CustomFile implements Resizable<HeroProfileImage> {
  public HeroProfileImage(Hero hero, MultipartFile file) {
    super(
        HERO_PROFILE_IMAGE_BASE_PATH_FORMAT.formatted(hero.getHeroNo().toString()),
        file);
  }

  @Builder
  public HeroProfileImage(HeroProfileImage heroProfileImage, byte[] bytes) {
    super(heroProfileImage, bytes);
  }

  @Override
  public HeroProfileImage resize() {
    var customRes = new IVSize();
    customRes.setWidth(HERO_IMAGE_RESIZING_WIDTH);
    customRes.setHeight(HERO_IMAGE_RESIZING_HEIGHT);

    try {
      var resizeImg = new IVCompressor()
                      .resizeImageWithCustomRes(bytes, ImageFormats.JPEG, customRes);

      return HeroProfileImage
          .builder()
          .heroProfileImage(this)
          .bytes(resizeImg)
          .build();

    } catch (ImageException e) {
      throw new RuntimeException(e);
    }
  }
}
