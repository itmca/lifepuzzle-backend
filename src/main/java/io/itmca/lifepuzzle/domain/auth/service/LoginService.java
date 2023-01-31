package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.Login;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.LoginResponse;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final TokenIssueService tokenIssueService;
    private final HeroQueryService heroQueryServiceService;

    public LoginResponse getLoginResponse(Login login) {
        var user = login.getUser();
        var tokens = tokenIssueService.getTokensOfUser(user.getUserNo());
        var hero = heroQueryServiceService.findHeroByHeroNo(user.getRecentHeroNo());

        var socialToken = login.getSocialToken();
        var isNewUser = login.getIsNewUser();

        if (StringUtils.hasText(socialToken)) {
            tokens.addSocialToken(socialToken);
        }

        LoginResponse loginResponse = LoginResponse.from(user, tokens, hero);

        if (isNewUser != null) {
            return loginResponse.from(isNewUser);
        }

        return loginResponse;
    }
}
