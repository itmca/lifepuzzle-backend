package io.itmca.lifepuzzle.domain.hero.endpoint.response.dto;

import static io.itmca.lifepuzzle.global.constant.FileConstant.HERO_PROFILE_IMAGE_BASE_PATH_FORMAT;
import static io.itmca.lifepuzzle.global.constant.ServerConstant.S3_SERVER_HOST;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@Schema(title = "주인공 조회 DTO")
public class HeroQueryDTO {
  @Schema(description = "주인공키")
  private Long heroNo;
  @Schema(description = "이름")
  private String heroName;
  @Schema(description = "별칭")
  private String heroNickName;
  @Schema(description = "생일")
  private LocalDate birthday;
  @Schema(description = "대표제목")
  private String title;
  @Schema(description = "대표이미지")
  private String imageURL;
  @Nullable
  @Schema(description = "권한")
  private HeroAuthStatus auth;

  @Schema(description = "양음력여부")
  private Boolean isLunar;

  public static HeroQueryDTO from(Hero hero, @Nullable Long userNo) {
    var heroAuth = hero.getHeroUserAuths().stream().filter(
        heroUserAuth -> heroUserAuth.isUserExist(userNo)
    ).findFirst().orElse(null);

    var auth = heroAuth != null ? heroAuth.getAuth() : null;

    return HeroQueryDTO.builder()
        .heroNo(hero.getHeroNo())
        .heroName(hero.getName())
        .heroNickName(hero.getNickname())
        .birthday(hero.getBirthday())
        .title(hero.getTitle())
        .imageURL(addServerHostInImage(hero.getHeroNo(), hero.getImage()))
        .auth(auth)
        .isLunar(hero.getIsLunar())
        .build();
  }

  public static HeroQueryDTO from(Hero hero) {
    return from(hero, null);
  }

  private static String addServerHostInImage(Long heroNo, String imageURL) {
    if (StringUtils.isBlank(imageURL)) {
      return "";
    }

    return S3_SERVER_HOST
        + HERO_PROFILE_IMAGE_BASE_PATH_FORMAT.formatted(String.valueOf(heroNo))
        + imageURL;
  }
}
