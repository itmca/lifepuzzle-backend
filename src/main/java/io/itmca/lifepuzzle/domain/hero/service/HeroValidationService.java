package io.itmca.lifepuzzle.domain.hero.service;

import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.repository.HeroUserAuthQueryRepository;
import io.itmca.lifepuzzle.global.exception.UserNotAccessibleToHeroException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeroValidationService {
    private final HeroUserAuthQueryRepository heroUserAuthQueryRepository;

    public void validateUserCanAccessedHero(Long userNo, Long heroNo) {
        List<HeroUserAuth> heroUserAuths = heroUserAuthQueryRepository.findByUserNoAndHeroNo(userNo, heroNo);

        if(heroUserAuths.isEmpty()) throw new UserNotAccessibleToHeroException();
    }
}
