package io.itmca.lifepuzzle.domain.hero.repository;

import io.itmca.lifepuzzle.global.type.Hero;

import javax.persistence.EntityManager;

public class HeroWriteRepository {
    private final EntityManager entityManager;

    public Hero create(Hero hero){}
    public Hero update(Hero hero){}
    public Hero remove(int heroNo){};
}
