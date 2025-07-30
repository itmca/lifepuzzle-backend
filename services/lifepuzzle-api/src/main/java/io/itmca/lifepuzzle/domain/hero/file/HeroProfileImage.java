package io.itmca.lifepuzzle.domain.hero.file;

import static io.itmca.lifepuzzle.global.constants.FileConstant.HERO_IMAGE_RESIZING_LONG_SIDE;
import static io.itmca.lifepuzzle.global.constants.FileConstant.HERO_IMAGE_RESIZING_SHORT_SIDE;
import static io.itmca.lifepuzzle.global.constants.FileConstant.HERO_PROFILE_IMAGE_BASE_PATH_FORMAT;

import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.ImageFormats;
import io.github.techgnious.exception.ImageException;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.global.file.CustomFile;
import io.itmca.lifepuzzle.global.file.Resizable;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Slf4j
public class HeroProfileImage extends CustomFile implements Resizable<HeroProfileImage> {
  public HeroProfileImage(Hero hero, MultipartFile file, String postfix) {
    super(
        HERO_PROFILE_IMAGE_BASE_PATH_FORMAT.formatted(
            // TODO: HeroTest 실패 원인 확인 후 null 방어 코드 제거
            hero.getHeroNo() != null ? hero.getHeroNo().toString() : "temp"
        ),
        file,
        postfix
    );
  }

  @Builder
  public HeroProfileImage(HeroProfileImage heroProfileImage, byte[] bytes) {
    super(heroProfileImage, bytes);
  }

  @Override
  public Optional<HeroProfileImage> resize() {
    var customRes = new IVSize();
    customRes.setWidth(HERO_IMAGE_RESIZING_LONG_SIDE);
    customRes.setHeight(HERO_IMAGE_RESIZING_SHORT_SIDE);

    try {
      var resizeImg = new IVCompressor()
          .resizeImageWithCustomRes(bytes, ImageFormats.JPEG, customRes);

      return Optional.of(HeroProfileImage
          .builder()
          .heroProfileImage(this)
          .bytes(resizeImg)
          .build());

    } catch (ImageException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<HeroProfileImage> resize(int fixedWidth) {
    // TODO 구현 필요
    return Optional.empty();
  }
}
