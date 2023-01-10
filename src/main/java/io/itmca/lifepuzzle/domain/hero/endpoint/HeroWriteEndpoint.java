package io.itmca.lifepuzzle.domain.hero.endpoint;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.hero.endpoint.request.HeroWriteRequest;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import io.itmca.lifepuzzle.domain.hero.service.HeroWriteService;
import io.itmca.lifepuzzle.global.constant.FileConstant;
import io.itmca.lifepuzzle.global.infra.file.S3Repository;
import io.itmca.lifepuzzle.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("hero")
@RequiredArgsConstructor
public class HeroWriteEndpoint {

    private final HeroValidationService heroValidationService;
    private final HeroWriteService heroWriteService;
    private final HeroUserAuthWriteService heroUserAuthWriteService;
    private final S3Repository s3Repository;

    @PostMapping("")
    public HeroQueryResponse createHero(@RequestPart("toCreate") Optional<HeroWriteRequest> heroWriteRequest,
                                        @RequestPart(value = "photo", required = false) MultipartFile photo,
                                        @AuthenticationPrincipal AuthPayload authPayload) throws IOException {

        Hero hero = null;

        if(!FileUtil.isExistFolder(FileConstant.TEMP_FOLDER_PATH)) {
            FileUtil.createAllFolder(FileConstant.TEMP_FOLDER_PATH);
        }

        if(photo != null && !photo.isEmpty()){
            hero = heroWriteService.create(heroWriteRequest.get()
                    .toHeroOf(FileUtil.addRandomValueFilePrefix(photo.getOriginalFilename())));

            heroWriteService.saveHeroFile(hero, photo);
        } else {
            hero = heroWriteService.create(heroWriteRequest.get().toHero());
        }

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
                              @RequestPart("toUpdate") Optional<HeroWriteRequest> heroWriteRequest,
                              @RequestPart(name = "photo") MultipartFile photo,
                              @AuthenticationPrincipal AuthPayload authPayload) throws IOException {
        heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);

        var hero = heroWriteRequest.get().toHeroOf(heroNo, FileUtil.addRandomValueFilePrefix(photo.getOriginalFilename()));
        heroWriteService.saveHeroFile(hero, photo);

        return HeroQueryResponse.from(heroWriteService.update(hero));

    }
}
