package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.endpoint.response.LoginResponse;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.hero.entity.Hero;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import io.itmca.lifepuzzle.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final TokenIssueService tokenIssueService;
    private final HeroQueryService heroQueryServiceService;

    public LoginResponse getLoginResponse(User user) {
        var tokens = tokenIssueService.getTokensOfUser(user.getUserNo());
        var hero = HeroQueryResponse.from(new Hero());
        //var hero = heroQueryServiceService.findHeroByUserValidation(1L);

        return LoginResponse.from(user, tokens, hero);
    }
}
