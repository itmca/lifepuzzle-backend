package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.repository.HeroUserAuthWriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeroUserAuthWriteService {

    private final HeroUserAuthWriteRepository heroUserAuthWriteRepository;

    public HeroUserAuth create(HeroUserAuth heroUserAuth){
        return heroUserAuthWriteRepository.save(heroUserAuth);
    }

    public HeroUserAuth update(HeroUserAuth heroUserAuth){
        return heroUserAuthWriteRepository.save(heroUserAuth);
    }

    public void remove(HeroUserAuth heroUserAuth){
        heroUserAuthWriteRepository.delete(heroUserAuth);
    }
}
