package io.itmca.lifepuzzle.domain.hero.endpoint.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record HeroWriteRequest(
    @JsonAlias("heroName")
    String name,
    @JsonAlias("heroNickName")
    String nickName,
    LocalDate birthday,
    String title,
    Boolean isLunar,
    @JsonProperty("isProfileImageUpdate")
    boolean profileImageUpdate
) {
  public HeroWriteRequest {
    if (isLunar == null) {
      isLunar = false;
    }
  }

  public Hero toHero() {
    return Hero.builder()
        .name(name)
        .nickname(nickName)
        .birthday(birthday)
        .isLunar(isLunar)
        .title(title)
        .build();
  }
}
