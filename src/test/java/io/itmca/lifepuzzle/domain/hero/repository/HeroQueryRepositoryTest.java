package io.itmca.lifepuzzle.domain.hero.repository;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
class HeroQueryRepositoryTest {

    @Autowired
    private HeroQueryRepository heroQueryRepository;

    @DisplayName("DB 유저 기준으로 전체 주인공 가져오기 테스트")
    @Test
    public void getAllHeroWithUserTest() {
        HeroUserAuth[] heroUserAuths = heroQueryRepository.findAllByUserNo(4L);

        for(HeroUserAuth heroUserAuth : heroUserAuths) {
            Hero hero = heroUserAuth.getHero();
            System.out.println(hero);
        }

//        System.out.println(heroUserAuth.getHero());
    };

    @DisplayName("DB 주인공 번호와 유저 번호로 주인공 1개 가져오기 테스트")
    @Test
    public void getHeroWithUserTest() {
        Hero hero = Hero.builder().heroNo(2L).build();

        Optional<HeroUserAuth> heroUserAuth = heroQueryRepository.findByUserNoAndHero(4L, hero);
        Hero findHero = heroUserAuth.get().getHero();
        Assertions.assertThat(findHero.getHeroNo()).isEqualTo(2L);
    };

    @DisplayName("[실패 케이스] DB 없는 주인공 번호와 유저 번호로 주인공 1개 가져오기 테스트")
    @Test
    public void getAllHeroWithUserInDBTest() {
        Hero hero = Hero.builder().heroNo(1L).build();

        Optional<HeroUserAuth> heroUserAuth = heroQueryRepository.findByUserNoAndHero(4L, hero);
        assertThrows(NoSuchElementException.class, () -> {heroUserAuth.get();});
    };

}