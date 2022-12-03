package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.endpoint.response.LoginResponse;
import io.itmca.lifepuzzle.domain.auth.entity.Token;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserQueryService userQueryService;
    private final TokenIssueService tokenIssueService;

    public LoginResponse getLoginResponse(String userId) {

        //User user = this.userQueryService.findByUserId(userId);
        User user = new User();
        Token tokens = this.tokenIssueService.getTokensOfUser(userId);

        return LoginResponse.from(user, tokens);
    }
}
