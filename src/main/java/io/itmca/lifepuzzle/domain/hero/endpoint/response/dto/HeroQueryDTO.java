package io.itmca.lifepuzzle.domain.hero.endpoint.response.dto;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.global.constant.ServerConstant;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class HeroQueryDTO {
  private Long heroNo;
  private String heroName;
  private String heroNickName;
  private LocalDate birthday;
  private String title;
  private String imageURL;

  public static HeroQueryDTO from(Hero hero) {
    return HeroQueryDTO.builder()
        .heroNo(hero.getHeroNo())
        .heroName(hero.getName())
        .heroNickName(hero.getNickname())
        .birthday(hero.getBirthday())
        .title(hero.getTitle())
        .imageURL(addServerHostInImage(hero.getHeroNo(), hero.getImage()))
        .build();
  }

  private static String addServerHostInImage(Long heroNo, String imageURL) {
    return String.format("%s/hero/profile/%d/%s", ServerConstant.SERVER_HOST, heroNo, imageURL);
  }
}
