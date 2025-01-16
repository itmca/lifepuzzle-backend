package io.itmca.lifepuzzle.domain.hero.endpoint.response.dto;

import static io.itmca.lifepuzzle.global.constant.FileConstant.HERO_PROFILE_IMAGE_BASE_PATH_FORMAT;
import static io.itmca.lifepuzzle.global.constant.ServerConstant.S3_SERVER_HOST;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HeroDTO {
  private Long id;
  private String name;
  private String nickname;
  private LocalDate birthdate;
  private int age;
  private String image;

  public static HeroDTO from(Hero hero) {
    return HeroDTO.builder()
      .id(hero.getHeroNo())
      .name(hero.getName())
      .nickname(hero.getNickname())
      .birthdate(hero.getBirthday())
      .age((int) ChronoUnit.YEARS.between(hero.getBirthday(), LocalDate.now()))
      .image(toFullImageUrl(hero.getHeroNo(), hero.getImage()))
      .build();
  }

  private static String toFullImageUrl(Long heroNo, String imageURL) {
    if (StringUtils.isBlank(imageURL)) {
      return "";
    }

    return S3_SERVER_HOST
        + HERO_PROFILE_IMAGE_BASE_PATH_FORMAT.formatted(String.valueOf(heroNo))
        + imageURL;
  }
}
