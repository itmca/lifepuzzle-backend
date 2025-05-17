package io.itmca.lifepuzzle.domain.user.endpoint;

import io.itmca.lifepuzzle.domain.hero.service.HeroValidationService;
import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserRecentHeroRequest;
import io.itmca.lifepuzzle.domain.user.endpoint.response.UserHeroShareResponse;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import io.itmca.lifepuzzle.global.resolver.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "현재 주인공 변경")
public class UserHeroEndpoint {
  private final UserWriteService userWriteService;
  private final HeroValidationService heroValidationService;

  @Operation(summary = "현재 주인공 변경")
  @PostMapping({"/user/hero/recent", // TODO: FE 전환 후 제거
      "/v1/users/hero/recent"})
  public void updateRecentHero(@RequestBody UserRecentHeroRequest request,
                               @CurrentUser User user) {
    Long heroNo = request.heroNo();
    heroValidationService.validateUserCanAccessHero(user.getId(), heroNo);

    userWriteService.changeRecentHeroNo(user, heroNo);
  }

  @Operation(summary = "주인공 권한 링크 조회")
  @PostMapping({"/user/hero/link", // TODO: FE 전환 후 제거
      "/v1/users/hero/link"})
  public UserHeroShareResponse getHeroAuthLink(@RequestParam("heroNo") Long heroNo,
                                               @RequestParam("auth") HeroAuthStatus shareAuth,
                                               @CurrentUser User user) {
    String shareLink = userWriteService.createHeroShareLink(user, heroNo, shareAuth);
    return UserHeroShareResponse.builder().link(shareLink).build();
  }


}
