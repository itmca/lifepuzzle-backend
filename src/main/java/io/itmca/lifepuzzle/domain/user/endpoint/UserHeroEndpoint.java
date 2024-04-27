package io.itmca.lifepuzzle.domain.user.endpoint;

import io.itmca.lifepuzzle.domain.hero.type.HeroAuthStatus;
import io.itmca.lifepuzzle.domain.user.CurrentUser;
import io.itmca.lifepuzzle.domain.user.endpoint.response.UserHeroShareResponse;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "현재 주인공 변경")
public class UserHeroEndpoint {

  //[Feedback] 사용하지 않는 멤버 변수가 존재
  private final UserWriteService userWriteService;

  @PostMapping("/user/hero/recent")
  @Operation(summary = "현재 주인공 변경")
  public void updateRecentHero(@RequestBody RecentHeroResponse recentHeroResponse,
                               @CurrentUser User user) {
    userWriteService.changeRecentHeroNo(user, recentHeroResponse.heroNo);
  }

  @Operation(summary = "주인공 권한 링크 조회")
  @PostMapping("/user/hero/link")
  public UserHeroShareResponse getHeroAuthLink(@RequestParam("heroNo") Long heroNo,
                                               @RequestParam("auth") HeroAuthStatus shareAuth,
                                               @CurrentUser User user) {
    String shareLink = userWriteService.createHeroShareLink(user, heroNo, shareAuth);
    return UserHeroShareResponse.builder().link(shareLink).build();
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  private static class RecentHeroResponse {
    Long heroNo;
  }
}
