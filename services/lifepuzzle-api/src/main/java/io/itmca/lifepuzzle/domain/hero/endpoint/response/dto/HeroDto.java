package io.itmca.lifepuzzle.domain.hero.endpoint.response.dto;

import static io.itmca.lifepuzzle.global.constants.FileConstant.HERO_PROFILE_IMAGE_BASE_PATH_FORMAT;
import static io.itmca.lifepuzzle.global.constants.ServerConstant.S3_SERVER_HOST;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Gallery API 응답용 Hero DTO.
 *
 * @deprecated Gallery API 응답에서 사용되던 Hero DTO입니다.
 *             향후 제거될 예정이며, {@link HeroQueryDto}를 사용하세요.
 */
@Deprecated(since = "2024.11", forRemoval = true)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HeroDto {
  private Long id;
  private String name;
  private String nickname;
  private LocalDate birthdate;
  private int age;
  private String image;

  public static HeroDto from(Hero hero) {
    return HeroDto.builder()
        .id(hero.getHeroNo())
        .name(hero.getName())
        .nickname(hero.getNickname())
        .birthdate(hero.getBirthday())
        .age((int) ChronoUnit.YEARS.between(hero.getBirthday(), LocalDate.now()))
        .image(toFullImageUrl(hero.getHeroNo(), hero.getImage()))
        .build();
  }

  private static String toFullImageUrl(Long heroNo, String imageUrl) {
    if (StringUtils.isBlank(imageUrl)) {
      return "";
    }

    return S3_SERVER_HOST
        + HERO_PROFILE_IMAGE_BASE_PATH_FORMAT.formatted(String.valueOf(heroNo))
        + imageUrl;
  }
}
