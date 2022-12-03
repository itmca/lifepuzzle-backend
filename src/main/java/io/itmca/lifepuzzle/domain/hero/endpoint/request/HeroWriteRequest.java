package io.itmca.lifepuzzle.domain.hero.endpoint.request;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HeroWriteRequest {

    private Long heroNo;
    private String heroName;
    private String heroNickName;
    private LocalDateTime birthday;
    private String title;
    private String imageURL;

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
