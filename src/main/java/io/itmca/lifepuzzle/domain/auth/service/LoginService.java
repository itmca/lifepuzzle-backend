package io.itmca.lifepuzzle.domain.auth.service;

import io.itmca.lifepuzzle.domain.auth.endpoint.LoginResponse;
import io.itmca.lifepuzzle.domain.auth.entity.Token;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private UserQueryService userQueryService;
    private TokenIssueService tokenIssueService;

    @Autowired
    public LoginService(UserQueryService userQueryService, TokenIssueService tokenIssueService) {
        this.userQueryService = userQueryService;
        this.tokenIssueService = tokenIssueService;
    }

    public LoginResponse getLoginResponse(String userId) {

        //User user = this.userQueryService.findByUserId(userId);
        User user = new User();
        Token tokens = this.tokenIssueService.getTokensOfUser(userId);

        return LoginResponse.from(user, tokens);
    }
}
