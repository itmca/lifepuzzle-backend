package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.Login;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.LoginResponse;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.dto.TokenQueryDto;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.HeroQueryResponse;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
import io.itmca.lifepuzzle.domain.user.endpoint.response.dto.UserQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class LoginService {

  private final TokenIssueService tokenIssueService;
  private final HeroQueryService heroQueryServiceService;
  private final HeroUserAuthWriteService heroUserAuthWriteService;

  public LoginResponse getLoginResponse(Login login) {
    var user = login.getUser();
    var tokens = tokenIssueService.getTokensOfUser(user.getId());

    // Only fetch hero if user has a recent hero set
    HeroQueryResponse heroResponse = null;
    if (user.getRecentHero() != null) {
      var hero = heroQueryServiceService.findHeroByHeroNo(user.getRecentHero());
      heroResponse = HeroQueryResponse.from(hero, user.getId());
    }

    var socialToken = login.getSocialToken();
    var isNewUser = login.getIsNewUser();

    if (StringUtils.hasText(socialToken)) {
      tokens.addSocialToken(socialToken);
    }

    var tokenQueryDTO = new TokenQueryDto(
        tokens.getAccessToken(),
        tokens.getRefreshToken(),
        tokens.getSocialToken()
    );

    var userQueryDTO = UserQueryDto.from(user);

    return new LoginResponse(
        userQueryDTO,
        tokenQueryDTO,
        heroResponse,
        isNewUser
    );
  }
}
