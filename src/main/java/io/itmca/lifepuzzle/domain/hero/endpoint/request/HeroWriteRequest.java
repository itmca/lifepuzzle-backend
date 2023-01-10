package io.itmca.lifepuzzle.domain.hero.endpoint.request;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.global.constant.ServerConstant;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HeroWriteRequest {

    private Long heroNo;
    private String heroName;
    private String heroNickName;
    private LocalDate birthday;
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
                .image(removeFileServerURLInImage())
                .build();
    }

    private String removeFileServerURLInImage(){
        var fileServerURL = String.format("%s/hero/profile/%d/", ServerConstant.SERVER_HOST, heroNo);
        return imageURL.replace(fileServerURL, "");
    }

    public Hero toHeroOf(String imageURL){
        return Hero.builder()
                .heroNo(heroNo)
                .name(heroName)
                .nickname(heroNickName)
                .birthday(birthday)
                .title(title)
                .image(imageURL)
                .build();
    }

    public Hero toHeroOf(Long heroNo, String imageURL){
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
