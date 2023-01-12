package io.itmca.lifepuzzle.domain.hero.endpoint;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.hero.endpoint.request.HeroWriteRequest;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import io.itmca.lifepuzzle.domain.hero.service.HeroWriteService;
import io.itmca.lifepuzzle.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("hero")
@RequiredArgsConstructor
public class HeroWriteEndpoint {

    private final HeroValidationService heroValidationService;
    private final HeroWriteService heroWriteService;
    private final HeroUserAuthWriteService heroUserAuthWriteService;

    @PostMapping("")
    public HeroQueryResponse createHero(@RequestPart("toCreate") HeroWriteRequest heroWriteRequest,
                                        @RequestPart(value = "photo", required = false) MultipartFile photo,
                                        @AuthenticationPrincipal AuthPayload authPayload) throws IOException {

        var hero = heroWriteService.create(heroWriteRequest.toHeroOf(photo));

        if(FileUtil.isMultiPartFile(photo)) heroWriteService.saveHeroFile(hero, photo);

        heroUserAuthWriteService.create(HeroUserAuth.builder()
                .userNo(authPayload.getUserNo())
                .hero(hero)
                .build());

        return HeroQueryResponse.from(hero);
    }

    @PutMapping("/{heroNo}")
    public HeroQueryResponse updateHero(@RequestBody HeroWriteRequest heroWriteRequest,
                                        @PathVariable("heroNo") Long heroNo,
                                        @AuthenticationPrincipal AuthPayload authPayload) {
        heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);

        return HeroQueryResponse.from(heroWriteService.create(heroWriteRequest.toHeroOf(heroNo)));
    }

    @DeleteMapping("/{heroNo}")
    public void deleteHeroAndHeroUserAuth(@PathVariable("heroNo") Long heroNo,
                                          @AuthenticationPrincipal AuthPayload authPayload) {
        heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);
        heroWriteService.remove(heroNo);
    }

    @PostMapping("/profile/{heroNo}")
    public HeroQueryResponse saveHeroPhoto(@PathVariable("heroNo") Long heroNo,
                              @RequestPart("toUpdate") HeroWriteRequest heroWriteRequest,
                              @RequestPart(name = "photo") MultipartFile photo,
                              @AuthenticationPrincipal AuthPayload authPayload) throws IOException {
        heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);

        var hero = heroWriteRequest.toHeroOf(heroNo, photo);
        heroWriteService.saveHeroFile(hero, photo);

        return HeroQueryResponse.from(heroWriteService.update(hero));

    }
}
