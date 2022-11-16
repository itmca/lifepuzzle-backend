package io.itmca.lifepuzzle.domain.hero.endpoint;

import io.itmca.lifepuzzle.domain.hero.endpoint.request.HeroWriteRequest;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.service.HeroWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("hero")
public class HeroWriteEndpoint {

    HeroWriteService heroWriteService;

    @Autowired
    public HeroWriteEndpoint(HeroWriteService heroWriteService) {
        this.heroWriteService = heroWriteService;
    }

    @PostMapping("")
    public void createHero(@RequestBody Optional<HeroWriteRequest> heroWriteRequest) {
        Hero hero = heroWriteRequest.get().toHero();
        HeroUserAuth heroUserAuth = HeroUserAuth.getHeroUserAuthInstance();
        Long userNo = 4L;

        heroUserAuth.setUserNo(userNo);
        heroUserAuth.setHero(hero);

        heroWriteService.create(heroUserAuth);

    }

    @PutMapping("/{heroNo}")
    public void updateHero() {}

    @DeleteMapping("/{heroNo}")
    public void deleteHero() {}

    @PostMapping("/profile/{heroNo}")
    public void saveHeroPhoto() {}
}
