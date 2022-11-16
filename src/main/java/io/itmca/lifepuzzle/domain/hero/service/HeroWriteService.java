package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.repository.HeroWriteRepository;
import org.springframework.stereotype.Service;

@Service
public class HeroWriteService {

    private final HeroWriteRepository heroWriteRepository;

    public HeroWriteService(HeroWriteRepository heroWriteRepository) {
        this.heroWriteRepository = heroWriteRepository;
    }
    public HeroUserAuth create(HeroUserAuth heroUserAuth){
        return heroWriteRepository.save(heroUserAuth);
    }
    public Hero update(Hero hero){
        return null;
    }
    public Hero remove(int heroNo){
        return null;
    };
}
