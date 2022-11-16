package io.itmca.lifepuzzle.domain.hero.endpoint.request;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;

import java.time.LocalDateTime;

public class HeroWriteRequest {
    Long heroNo;
    String heroName;
    String heroNickName;
    LocalDateTime birthday;
    String title;
    String imageURL;
    public HeroWriteRequest(Long heroNo, String heroName, String heroNickName, LocalDateTime birthday, String title, String imageURL) {
        this.heroNo = heroNo;
        this.heroName = heroName;
        this.heroNickName = heroNickName;
        this.birthday = birthday;
        this.title = title;
        this.imageURL = imageURL;
    }

    public Hero toHero(){
        Hero hero = Hero.getHeroInstance();
        hero.setHeroId(heroNo);
        hero.setName(heroName);
        hero.setNickname(heroNickName);
        hero.setBirthday(birthday);
        hero.setTitle(title);
        hero.setImage(imageURL);

        return hero;
    }

    @Override
    public String toString() {
        return "HeroWriteRequest{" +
                "heroNo=" + heroNo +
                ", heroName='" + heroName + '\'' +
                ", heroNickName='" + heroNickName + '\'' +
                ", birthday=" + birthday +
                ", title='" + title + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
