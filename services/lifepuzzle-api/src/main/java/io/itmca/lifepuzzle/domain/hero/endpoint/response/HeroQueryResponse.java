package io.itmca.lifepuzzle.domain.hero.endpoint.response;

import static io.itmca.lifepuzzle.global.constants.FileConstant.HERO_PROFILE_IMAGE_BASE_PATH_FORMAT;
import static io.itmca.lifepuzzle.global.constants.ServerConstant.S3_SERVER_HOST;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

@Schema(title = "주인공 조회 응답")
public record HeroQueryResponse(
    @Schema(description = "주인공키") Long id,
    @Schema(description = "이름") String name,
    @Schema(description = "별칭") String nickName,
    @Schema(description = "생일") LocalDate birthday,
    @Schema(description = "대표이미지") String imageUrl,
    @Nullable @Schema(description = "권한") HeroAuthStatus auth,
    @Schema(description = "양음력여부") Boolean isLunar
) {

  public static HeroQueryResponse from(Hero hero, @Nullable Long userNo) {
    var heroAuth = hero.getHeroUserAuths().stream()
        .filter(heroUserAuth -> heroUserAuth.isUserExist(userNo))
        .findFirst()
        .orElse(null);

    var auth = heroAuth != null ? heroAuth.getAuth() : null;

    return new HeroQueryResponse(
        hero.getHeroNo(),
        hero.getName(),
        hero.getNickname(),
        hero.getBirthdate(),
        addServerHostInImage(hero.getHeroNo(), hero.getImage()),
        auth,
        hero.getIsLunar()
    );
  }

  public static HeroQueryResponse from(Hero hero) {
    return from(hero, null);
  }

  private static String addServerHostInImage(Long heroNo, String imageUrl) {
    if (StringUtils.isBlank(imageUrl)) {
      return "";
    }

    return S3_SERVER_HOST
        + HERO_PROFILE_IMAGE_BASE_PATH_FORMAT.formatted(String.valueOf(heroNo))
        + imageUrl;
  }
}
