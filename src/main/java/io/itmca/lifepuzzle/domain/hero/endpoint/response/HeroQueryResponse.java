package io.itmca.lifepuzzle.domain.hero.endpoint.response;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import lombok.*;

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

}
