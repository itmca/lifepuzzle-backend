package io.itmca.lifepuzzle.domain.hero.endpoint.response.dto;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
