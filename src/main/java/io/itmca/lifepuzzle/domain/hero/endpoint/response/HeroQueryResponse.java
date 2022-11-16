package io.itmca.lifepuzzle.domain.hero.endpoint.response;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;

import java.time.LocalDateTime;
public class HeroQueryResponse {

    private HeroQueryResponse() {};
    public static HeroQueryDTO from(Hero hero) {
        return new HeroQueryDTO(hero);
    }

    private static class HeroQueryDTO extends HeroQueryResponse {
        public Long number;
        public String heroName;
        public String heroNickName;
        public LocalDateTime birthday;
        public String title;
        public String imageURL;
        public HeroQueryDTO(Hero hero) {
            this.number = hero.getHeroId();
            this.heroName = hero.getName();
            this.heroNickName = hero.getNickname();
            this.birthday = hero.getBirthday();
            this.title = hero.getTitle();
            this.imageURL = hero.getImage();
        }
    }
}
