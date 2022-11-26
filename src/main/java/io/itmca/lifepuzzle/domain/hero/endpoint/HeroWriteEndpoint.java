package io.itmca.lifepuzzle.domain.hero.endpoint;

import io.itmca.lifepuzzle.domain.hero.endpoint.request.HeroWriteRequest;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.hero.service.HeroWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hero")
@RequiredArgsConstructor
public class HeroWriteEndpoint {

    private final HeroQueryService heroQueryService;
    private final HeroWriteService heroWriteService;
    private final HeroUserAuthWriteService heroUserAuthWriteService;
    @PostMapping("")
    public HeroQueryResponse createHero(@RequestBody HeroWriteRequest heroWriteRequest) {
        var hero = heroWriteService
                .create(heroWriteRequest.toHero());

        var userNo = 4L;

        heroUserAuthWriteService.create(HeroUserAuth.builder()
                .userNo(userNo)
                .hero(hero)
                .build());

        return HeroQueryResponse.from(hero);
    }

    @PutMapping("/{heroNo}")
    public HeroQueryResponse updateHero(@RequestBody HeroWriteRequest heroWriteRequest, @PathVariable("heroNo") Long heroNo) {
        var userNo = 4L;
        HeroUserAuth heroUserAuth = heroQueryService.findHeroUserAuth(userNo, heroNo);

        // 권환 체크?

        var hero = heroWriteService.create(heroWriteRequest.toHeroOf(heroNo));
        return HeroQueryResponse.from(hero);
    }

    @DeleteMapping("/{heroNo}")
    public void deleteHeroAndHeroUserAuth(@PathVariable("heroNo") Long heroNo) {
        var hero = HeroWriteRequest
                .builder()
                .build()
                .toHeroOf(heroNo);

        var userNo = 4L;
        HeroUserAuth heroUserAuth = heroQueryService.findHeroUserAuth(userNo, heroNo);

        heroUserAuthWriteService.remove(heroUserAuth);
        heroWriteService.remove(hero);
    }

    @PostMapping("/profile/{heroNo}")
    public void saveHeroPhoto() {}
}
