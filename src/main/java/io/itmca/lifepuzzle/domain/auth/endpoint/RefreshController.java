package io.itmca.lifepuzzle.domain.auth.endpoint;

import io.itmca.lifepuzzle.domain.auth.Token;
import io.itmca.lifepuzzle.domain.auth.service.RefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class RefreshController {
    private final RefreshService refreshService;

    @PostMapping("/auth/refresh")
    public Token refresh(HttpServletRequest req) {
        var authorization = req.getHeader("authorization");
        var refreshToken = authorization.replace("Bearer ", "");

        return refreshService.refresh(refreshToken);
    }
}
