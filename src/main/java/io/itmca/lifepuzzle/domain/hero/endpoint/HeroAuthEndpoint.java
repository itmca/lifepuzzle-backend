package io.itmca.lifepuzzle.domain.hero.endpoint;

import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.ADMIN;
import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.OWNER;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.hero.endpoint.request.HeroChangeAuthRequest;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.global.aop.AuthCheck;
import io.itmca.lifepuzzle.global.aop.HeroNo;
import io.itmca.lifepuzzle.global.aop.HeroNoContainer;
import io.itmca.lifepuzzle.global.resolver.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "주인공 권한 제어")
public class HeroAuthEndpoint {
  private final HeroValidationService heroValidationService;
  private final HeroUserAuthWriteService heroUserAuthWriteService;

  @Operation(summary = "요청 본인에 대한 주인공 권한 추가")
  @PostMapping({"/v1/heroes/auth"})
  public void addMyHeroAuth(
      @RequestParam String shareKey,
      @CurrentUser User user
  ) {
    heroUserAuthWriteService.createByShareKey(user, shareKey);
  }

  @AuthCheck(auths = {OWNER, ADMIN})
  @Operation(summary = "주인공에 대한 다른 유저의 권한 수정")
  @PutMapping({"/v1/heroes/auth"})
  public void changeOtherUserHeroAuth(
      @HeroNoContainer @RequestBody HeroChangeAuthRequest heroChangeAuthRequest,
      @AuthenticationPrincipal AuthPayload authPayload
  ) {
    heroValidationService.validateUserCanAccessHero(authPayload.getUserId(),
        heroChangeAuthRequest.heroNo());

    heroUserAuthWriteService.update(heroChangeAuthRequest);
  }

  @AuthCheck(auths = {OWNER, ADMIN})
  @Operation(summary = "주인공에 대한 다른 유저의 권한 삭제")
  @DeleteMapping({"/v1/heroes/auth"})
  public void deleteOtherUserHeroAuth(
      @RequestParam Long userNo,
      @HeroNo @RequestParam Long heroNo,
      @AuthenticationPrincipal AuthPayload authPayload
  ) {
    heroValidationService.validateUserCanAccessHero(authPayload.getUserId(), heroNo);

    heroUserAuthWriteService.removeByUserAndHero(userNo, heroNo);
  }
}
