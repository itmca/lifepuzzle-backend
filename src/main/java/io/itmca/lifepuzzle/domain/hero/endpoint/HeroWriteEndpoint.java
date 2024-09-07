package io.itmca.lifepuzzle.domain.hero.endpoint;

import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.ADMIN;
import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.OWNER;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.hero.endpoint.request.HeroChangeAuthRequest;
import io.itmca.lifepuzzle.domain.hero.endpoint.request.HeroWriteRequest;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroQueryDTO;
import io.itmca.lifepuzzle.domain.hero.file.HeroProfileImage;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import io.itmca.lifepuzzle.domain.hero.service.HeroWriteService;
import io.itmca.lifepuzzle.domain.user.CurrentUser;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.global.aop.AuthCheck;
import io.itmca.lifepuzzle.global.aop.HeroNo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

  @Operation(summary = "주인공 등록")
  @PostMapping("/heroes")
  public HeroQueryDTO createHero(@RequestPart("toWrite") HeroWriteRequest request,
                                 @RequestPart(value = "photo", required = false)
                                 MultipartFile profile,
                                 @CurrentUser User user) {
    var hero = heroWriteService.create(request, user, profile);

    return HeroQueryDTO.from(hero);
  }

  @AuthCheck(auths = {ADMIN, OWNER})
  @Operation(summary = "주인공 수정")
  @PutMapping("heroes/{heroNo}")
  public HeroQueryDTO updateHero(
      @PathVariable("heroNo") @HeroNo Long heroNo,
      @RequestPart("toWrite")
      HeroWriteRequest request,
      @RequestPart(value = "photo", required = false)
      MultipartFile profile,
      @AuthenticationPrincipal
      AuthPayload authPayload) {
    heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);

    return HeroQueryDTO.from(heroWriteService.update(heroNo, request, profile));
  }

  @AuthCheck(auths = {OWNER})
  @Operation(summary = "주인공 삭제")
  @DeleteMapping("heroes/{heroNo}")
  public void deleteHero(@PathVariable("heroNo") @HeroNo Long heroNo,
                         @AuthenticationPrincipal AuthPayload authPayload) {
    heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);
    heroWriteService.remove(heroNo);
  }

  // TODO: FE에서 주인공 저장 시점에 사진도 저장하는 것으로 되어 Deprecated 되었으며 FE 전환 후 제거
  @Deprecated
  @AuthCheck(auths = {ADMIN, OWNER})
  @Operation(summary = "주인공 사진 수정")
  @RequestMapping(
      value = {"heroes/profile/{heroNo}", // TODO: FE 전환 후 제거
          "heroes/{heroNo}/profile"},
      method = {POST, PUT})
  public HeroQueryDTO saveHeroPhoto(@PathVariable("heroNo") @HeroNo Long heroNo,
                                    @RequestPart(name = "photo")
                                    MultipartFile profile,
                                    @AuthenticationPrincipal AuthPayload authPayload) {
    heroValidationService.validateUserCanAccessHero(authPayload.getUserNo(), heroNo);

    var updated = heroWriteService.updateProfile(heroNo, profile);

    return HeroQueryDTO.from(updated);

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
