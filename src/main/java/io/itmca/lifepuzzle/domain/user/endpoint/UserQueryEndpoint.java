package io.itmca.lifepuzzle.domain.user.endpoint;

import io.itmca.lifepuzzle.domain.user.endpoint.response.UserQueryResponse;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
//@RequiredArgsConstructor
public class UserQueryEndpoint {

    //private final UserQueryService userQueryService;

    @GetMapping("/{id}")
    public UserQueryResponse getOne(@PathVariable("id") Long id) {
        return new UserQueryResponse();
    }

}
