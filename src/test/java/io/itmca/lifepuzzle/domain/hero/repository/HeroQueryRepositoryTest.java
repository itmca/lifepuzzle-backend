package io.itmca.lifepuzzle.domain.hero.repository;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class HeroQueryRepositoryTest {

    @Autowired
    private HeroUserAuthQueryRepository heroUserAuthQueryRepository;
    @Autowired
    private HeroQueryRepository heroQueryRepository;

    @Test
    @Disabled
    @DisplayName("DB 유저 기준으로 전체 주인공 가져오기 테스트")
    public void getAllHeroWithUserTest() {
        List<HeroUserAuth> heroUserAuths = heroUserAuthQueryRepository.findAllByUserNo(4L);

        for (HeroUserAuth heroUserAuth : heroUserAuths) {
            Hero hero = heroUserAuth.getHero();
            System.out.println(hero);
        }

    }

    ;

    @Test
    @DisplayName("DB 주인공 번호와 유저 번호로 주인공 1개 가져오기 테스트")
    @Disabled
    public void getHeroWithUserTest() {

        Optional<Hero> hero = heroQueryRepository.findByHeroNo(4L);
        Assertions.assertThat(hero.get().getHeroNo()).isEqualTo(4L);
    }

    ;

    @Test
    @Disabled
    @DisplayName("[실패 케이스] DB 없는 주인공 번호와 유저 번호로 주인공 1개 가져오기 테스트")
    public void getAllHeroWithUserInDBTest() {
        Optional<Hero> hero = heroQueryRepository.findByHeroNo(1L);
        assertThrows(NoSuchElementException.class, () -> {
            hero.get();
        });
    }

    ;

}