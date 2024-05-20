package io.itmca.lifepuzzle.domain.hero.endpoint;

import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.ADMIN;
import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.OWNER;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.hero.endpoint.request.HeroChangeAuthRequest;
import io.itmca.lifepuzzle.domain.hero.endpoint.request.HeroWriteRequest;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroQueryDTO;
import io.itmca.lifepuzzle.domain.hero.entity.HeroUserAuth;
import io.itmca.lifepuzzle.domain.hero.file.HeroProfileImage;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import io.itmca.lifepuzzle.domain.hero.service.HeroWriteService;
import io.itmca.lifepuzzle.domain.user.CurrentUser;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.global.aop.AuthCheck;
import io.itmca.lifepuzzle.global.aop.HeroNo;
import io.itmca.lifepuzzle.global.infra.file.service.S3UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "주인공 등록")
public class HeroWriteEndpoint {

  private final HeroValidationService heroValidationService;
  private final HeroWriteService heroWriteService;
  private final HeroUserAuthWriteService heroUserAuthWriteService;
  private final S3UploadService s3UploadService;

  @Operation(summary = "주인공 등록")
  @PostMapping("/heroes")
  public HeroQueryDTO createHero(@RequestPart("toWrite") HeroWriteRequest heroWriteRequest,
                                 @RequestPart(value = "photo", required = false)
                                 MultipartFile requestPhoto,
                                 @CurrentUser User user) {

    var hero = heroWriteService.create(heroWriteRequest.toHeroOf(requestPhoto));

    if (requestPhoto != null) {
      var heroProfileImage = new HeroProfileImage(hero, requestPhoto);

      hero.setImage(heroProfileImage);

      s3UploadService.upload(heroProfileImage);
    }

    heroUserAuthWriteService.create(HeroUserAuth.builder()
        .user(user)
        .hero(hero)
        .auth(OWNER)
        .build());

    return HeroQueryDTO.from(hero);
  }

  @AuthCheck(auths = {ADMIN, OWNER})
  @Operation(summary = "주인공 수정")
  @PutMapping("heroes/{heroNo}")
  public HeroQueryDTO updateHero(@RequestBody HeroWriteRequest heroWriteRequest,
                                 @PathVariable("heroNo") @HeroNo Long heroNo,
                                 @AuthenticationPrincipal AuthPayload authPayload) {
    heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);

    return HeroQueryDTO.from(heroWriteService.create(heroWriteRequest.toHeroOf(heroNo)));
  }

  @AuthCheck(auths = {OWNER})
  @Operation(summary = "주인공 삭제")
  @DeleteMapping("heroes/{heroNo}")
  public void deleteHero(@PathVariable("heroNo") @HeroNo Long heroNo,
                         @AuthenticationPrincipal AuthPayload authPayload) {
    heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);
    heroWriteService.remove(heroNo);
  }

  @AuthCheck(auths = {ADMIN, OWNER})
  @Operation(summary = "주인공 사진 수정")
  @PutMapping("heroes/profile/{heroNo}")
  public HeroQueryDTO saveHeroPhoto(@PathVariable("heroNo") @HeroNo Long heroNo,
                                    @RequestPart("toUpdate") HeroWriteRequest heroWriteRequest,
                                    @RequestPart(name = "photo", required = false)
                                    MultipartFile requestPhoto,
                                    @AuthenticationPrincipal AuthPayload authPayload) {
    heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);

    var hero = heroWriteRequest.toHeroOf(heroNo, requestPhoto);

    if (requestPhoto != null) {
      var heroProfileImage = new HeroProfileImage(hero, requestPhoto);

      hero.setImage(heroProfileImage);

      s3UploadService.upload(heroProfileImage);
    }

    return HeroQueryDTO.from(heroWriteService.update(hero));

  }

  @Operation(summary = "유저의 주인공 권한 변경")
  @PutMapping("heroes/auth")
  public void changeHeroAuthOfUser(@RequestBody HeroChangeAuthRequest heroChangeAuthRequest,
                                   @AuthenticationPrincipal AuthPayload authPayload) {
    heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(),
        heroChangeAuthRequest.heroNo());
    heroUserAuthWriteService.update(heroChangeAuthRequest);
  }

  @Operation(summary = "유저의 주인공 권한 추가")
  @PostMapping("heroes/auth")
  public void createHeroAuthOfUser(@RequestParam String shareKey,
                                   @CurrentUser User user) {
    heroUserAuthWriteService.create(user, shareKey);
  }
}
