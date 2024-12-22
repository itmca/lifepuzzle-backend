package io.itmca.lifepuzzle.domain.hero.endpoint.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HeroWriteRequest {
  private String heroName;
  private String heroNickName;
  private LocalDate birthday;
  private String title;
  private boolean isLunar;
  @JsonProperty("isProfileImageUpdate")
  private boolean profileImageUpdate;

  public Hero toHero() {
    return Hero.builder()
        .name(heroName)
        .nickname(heroNickName)
        .birthday(birthday)
        .isLunar(isLunar)
        .title(title)
        .build();
  }
}
