package io.itmca.lifepuzzle.domain.hero.endpoint.request;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record HeroWriteRequest(
    String name,
    String nickName,
    LocalDate birthday,
    String title,
    Boolean isLunar,
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
