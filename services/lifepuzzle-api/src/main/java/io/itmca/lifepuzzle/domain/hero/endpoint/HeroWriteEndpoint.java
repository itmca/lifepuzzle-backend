package io.itmca.lifepuzzle.domain.hero.endpoint;

import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.ADMIN;
import static io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus.OWNER;

import io.itmca.lifepuzzle.domain.auth.jwt.AuthPayload;
import io.itmca.lifepuzzle.domain.hero.endpoint.request.HeroWriteRequest;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroQueryDto;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import io.itmca.lifepuzzle.domain.hero.service.HeroWriteService;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.global.aop.AuthCheck;
import io.itmca.lifepuzzle.global.aop.HeroNo;
import io.itmca.lifepuzzle.global.resolver.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  @PostMapping({"/v1/heroes"})
  public HeroQueryDto createHero(@RequestPart("toWrite") HeroWriteRequest request,
                                 @RequestPart(value = "photo", required = false)
                                 MultipartFile profile,
                                 @CurrentUser User user) {
    var hero = heroWriteService.create(request, user, profile);

    return HeroQueryDto.from(hero, user.getId());
  }

  @AuthCheck(auths = {ADMIN, OWNER})
  @Operation(summary = "주인공 수정")
  @PutMapping({"/v1/heroes/{heroNo}"})
  public HeroQueryDto updateHero(
      @PathVariable("heroNo") @HeroNo Long heroNo,
      @RequestPart("toWrite")
      HeroWriteRequest request,
      @RequestPart(required = false)
      MultipartFile photo,
      @AuthenticationPrincipal
      AuthPayload authPayload) {
    return HeroQueryDto.from(heroWriteService.update(heroNo, request, photo));
  }

  @AuthCheck(auths = {OWNER})
  @Operation(summary = "주인공 삭제")
  @DeleteMapping({"/v1/heroes/{heroNo}"})
  public void deleteHero(@PathVariable("heroNo") @HeroNo Long heroNo,
                         @AuthenticationPrincipal AuthPayload authPayload) {
    heroValidationService.validateUserCanAccessHero(authPayload.getUserId(), heroNo);
    heroWriteService.delete(heroNo);
  }
}
