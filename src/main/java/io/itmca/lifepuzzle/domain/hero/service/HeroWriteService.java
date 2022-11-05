package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.repository.HeroWriteRepository;
import io.itmca.lifepuzzle.global.type.Hero;

public class HeroWriteService {

    public HeroWriteService(HeroWriteRepository heroWriteRepository) {
        this.heroWriteRepository = heroWriteRepository;
    }

    private final HeroWriteRepository heroWriteRepository;
    public Hero create(Hero hero){}
    public Hero update(Hero hero){}
    public Hero remove(int heroNo){};
}
