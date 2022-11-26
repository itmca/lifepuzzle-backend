package io.itmca.lifepuzzle.domain.hero.endpoint.request;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@ToString
public class HeroWriteRequest {

    private final Long heroNo;
    private final String heroName;
    private final String heroNickName;
    private final LocalDateTime birthday;
    private final String title;
    private final String imageURL;

    public Hero toHero(){
        return toHeroOf(heroNo);
    }

    public Hero toHeroOf(Long heroNo){
        return Hero.builder()
                .heroNo(heroNo)
                .name(heroName)
                .nickname(heroNickName)
                .birthday(birthday)
                .title(title)
                .image(imageURL)
                .build();
    }
}
