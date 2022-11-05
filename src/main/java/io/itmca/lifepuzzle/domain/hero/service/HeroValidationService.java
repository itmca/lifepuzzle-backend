package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.repository.HeroQueryRepository;
import io.itmca.lifepuzzle.domain.hero.repository.HeroWriteRepository;

public class HeroValidationService {
    private final HeroQueryRepository heroQueryRepository;
    private final HeroWriteRepository heroWriteRepository;

    public HeroValidationService(HeroQueryRepository heroQueryRepository, HeroWriteRepository heroWriteRepository) {
        this.heroQueryRepository = heroQueryRepository;
        this.heroWriteRepository = heroWriteRepository;
    }

    public HeroUserAuth create(HeroUserAuth heroUserAuth){}
    public boolean isValidation(int userNo, int heroNo) {};
}
