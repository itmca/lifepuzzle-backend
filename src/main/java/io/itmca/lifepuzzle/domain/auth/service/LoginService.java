package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.Login;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.LoginResponse;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final TokenIssueService tokenIssueService;
    private final HeroQueryService heroQueryServiceService;

    public LoginResponse getLoginResponse(Login login) {
        var user = login.getUser();
        var socialToken = login.getSocialToken();
        var isNewUser = login.getIsNewUser();

        var tokens = tokenIssueService.getTokensOfUser(user.getUserNo());
        var hero = HeroQueryResponse.from(heroQueryServiceService.findHeroByHeroNo(user.getRecentHeroNo()));

        if (StringUtils.hasText(socialToken)) {
            tokens.addSocialToken(socialToken);
        }

        LoginResponse loginResponse = LoginResponse.from(user, tokens, hero);

        if (!Objects.isNull(isNewUser)) {
            return loginResponse.from(isNewUser);
        }

        return loginResponse;
    }
}
