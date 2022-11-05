package io.itmca.lifepuzzle.domain.hero.repository;

import io.itmca.lifepuzzle.global.type.Hero;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class HeroQueryRepository {
    private final EntityManager entityManager;

    public Hero findByHeroNo(int herNo) {}
    public Hero findHeroesByUserNo(int userNo) {}
}
