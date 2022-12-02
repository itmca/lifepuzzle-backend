package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.repository.HeroWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeroWriteService {
    private final HeroWriteRepository heroWriteRepository;

    public Hero create(Hero hero){
        return heroWriteRepository.save(hero);
    }
    public Hero update(Hero hero){
        return heroWriteRepository.save(hero);
    }
    public void remove(Long heroNo){
        heroWriteRepository.deleteById(heroNo);
    };
}
