package io.itmca.lifepuzzle.domain.hero.endpoint.response;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.global.constant.ServerConstant;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HeroQueryResponse {

    private HeroQueryDTO hero;

    public static HeroQueryResponse from(Hero hero){
        return HeroQueryResponse.builder()
                .hero(HeroQueryDTO.from(hero))
                .build();
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    private static class HeroQueryDTO {
        private Long number;
        private String heroName;
        private String heroNickName;
        private LocalDate birthday;
        private String title;
        private String imageURL;

        public static HeroQueryDTO from(Hero hero) {
            return HeroQueryDTO.builder()
                    .number(hero.getHeroNo())
                    .heroName(hero.getName())
                    .heroNickName(hero.getNickname())
                    .birthday(hero.getBirthday())
                    .title(hero.getTitle())
                    .imageURL(addServerURLInImage(hero.getHeroNo(), hero.getImage()))
                    .build();
        }

        private static String addServerURLInImage(Long heroNo, String imageURL){
            return String.format("%s/hero/profile/%d/%s", ServerConstant.SERVER_HOST, heroNo, imageURL);
        }
    }
}
