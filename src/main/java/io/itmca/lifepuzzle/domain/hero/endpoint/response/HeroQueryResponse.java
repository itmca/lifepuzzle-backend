package io.itmca.lifepuzzle.domain.hero.endpoint.response;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HeroQueryResponse {

    private HeroQueryDTO hero;

    public static HeroQueryResponse from(Hero hero){
        return HeroQueryResponse.builder()
                .hero(HeroQueryDTO.from(hero))
                .build();
    }

    @Builder(access = AccessLevel.PRIVATE)
    @Getter
    private static class HeroQueryDTO {
        private Long number;
        private String heroName;
        private String heroNickName;
        private LocalDateTime birthday;
        private String title;
        private String imageURL;

        public static HeroQueryDTO from(Hero hero) {
            return HeroQueryDTO.builder()
                    .number(hero.getHeroNo())
                    .heroName(hero.getName())
                    .heroNickName(hero.getNickname())
                    .birthday(hero.getBirthday())
                    .title(hero.getTitle())
                    .imageURL(hero.getImage())
                    .build();
        }
    }
}
