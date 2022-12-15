package io.itmca.lifepuzzle.domain.user.endpoint;

import io.itmca.lifepuzzle.domain.user.CurrentUser;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.domain.user.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserHeroEndpoint {
    
    private final UserQueryService userQueryService;
    private final UserWriteService userWriteService;

    @PostMapping("/user/hero/recent")
    public void updateRecentHero(@RequestParam("heroNo") Long heroNo,
                                 @CurrentUser User user) {
        userWriteService.changeRecentHeroNo(user, heroNo);
    }
}
