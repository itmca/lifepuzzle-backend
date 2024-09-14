package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.Login;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.LoginResponse;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.dto.TokenQueryDTO;
import io.itmca.lifepuzzle.domain.auth.endpoint.response.dto.UserQueryDTO;
import io.itmca.lifepuzzle.domain.hero.endpoint.response.dto.HeroQueryDTO;
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
    var tokens = tokenIssueService.getTokensOfUser(user.getId());
    var hero = heroQueryServiceService.findHeroByHeroNo(user.getRecentHeroNo());

    var socialToken = login.getSocialToken();
    var isNewUser = login.getIsNewUser();

    if (StringUtils.hasText(socialToken)) {
      tokens.addSocialToken(socialToken);
    }

    var tokenQueryDTO = TokenQueryDTO.builder()
        .accessToken(tokens.getAccessToken())
        .accessTokenExpireAt(tokens.getAccessTokenExpireAt())
        .refreshToken(tokens.getRefreshToken())
        .refreshTokenExpireAt(tokens.getRefreshTokenExpireAt())
        .socialToken(tokens.getSocialToken())
        .build();

    var userQueryDTO = UserQueryDTO.builder()
        .userNo(user.getId())
        .userNickName(user.getNickName())
        .userType(user.getUserType())
        .build();

    return LoginResponse.builder()
        .user(userQueryDTO)
        .tokens(tokenQueryDTO)
        .hero(HeroQueryDTO.from(hero))
        .isNewUser(isNewUser)
        .build();
  }
}
