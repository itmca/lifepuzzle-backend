package io.itmca.lifepuzzle.domain.hero.endpoint;

import io.itmca.lifepuzzle.domain.hero.endpoint.request.HeroWriteRequest;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import io.itmca.lifepuzzle.domain.hero.service.HeroWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hero")
@RequiredArgsConstructor
public class HeroWriteEndpoint {

    private final HeroValidationService heroValidationService;
    private final HeroWriteService heroWriteService;
    private final HeroUserAuthWriteService heroUserAuthWriteService;

    @PostMapping("")
    public HeroQueryResponse createHero(@RequestBody HeroWriteRequest heroWriteRequest) {
        var dummyUserNo = 4L;
        var hero = heroWriteService
                .create(heroWriteRequest.toHero());

        heroUserAuthWriteService.create(HeroUserAuth.builder()
                .userNo(dummyUserNo)
                .hero(hero)
                .build());

        return HeroQueryResponse.from(hero);
    }

    @PutMapping("/{heroNo}")
    public HeroQueryResponse updateHero(@RequestBody HeroWriteRequest heroWriteRequest, @PathVariable("heroNo") Long heroNo) {
        var dummyUserNo = 4L;

        heroValidationService.validateUserCanAccessedHero(dummyUserNo, heroNo);

        return HeroQueryResponse.from(heroWriteService.create(heroWriteRequest.toHeroOf(heroNo)));
    }

    @DeleteMapping("/{heroNo}")
    public void deleteHeroAndHeroUserAuth(@PathVariable("heroNo") Long heroNo) {
        var dummyUserNo = 4L;

        heroValidationService.validateUserCanAccessedHero(dummyUserNo, heroNo);
        heroWriteService.remove(heroNo);
    }

    @PostMapping("/profile/{heroNo}")
    public void saveHeroPhoto() {}
}
