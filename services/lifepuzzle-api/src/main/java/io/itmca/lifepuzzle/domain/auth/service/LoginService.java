package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.Login;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.LoginResponse;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.dto.TokenQueryDto;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.dto.UserQueryDto;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroQueryDto;
import io.itmca.lifepuzzle.domain.hero.service.HeroQueryService;
import io.itmca.lifepuzzle.domain.hero.service.HeroUserAuthWriteService;
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
    var hero = heroQueryServiceService.findHeroByHeroNo(user.getRecentHeroNo());

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

    var userQueryDTO = UserQueryDto.builder()
        .userNo(user.getId())
        .userNickName(user.getNickName())
        .userType(user.getUserType())
        .build();

    return new LoginResponse(
        userQueryDTO,
        tokenQueryDTO,
        HeroQueryDto.from(hero, user.getId()),
        isNewUser
    );
  }
}
