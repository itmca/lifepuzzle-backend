package io.itmca.lifepuzzle.domain.user.endpoint;

import io.itmca.lifepuzzle.domain.auth.TokenType;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserQueryEndpoint {

    private final UserQueryService userQueryService;

    @GetMapping("/users/{id}")
    public User getOne(@PathVariable("id") Long id) {
        System.out.println(TokenType.ACCESS);
        return userQueryService.findByUserNo(id);
    }

}
