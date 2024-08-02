package io.itmca.lifepuzzle.domain.auth.endpoint;

import io.itmca.lifepuzzle.domain.auth.Token;
import io.itmca.lifepuzzle.domain.auth.service.RefreshService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "토큰 리프레시")
public class RefreshEndpoint {
  private final RefreshService refreshService;

  @PostMapping("/auth/refresh")
  @Operation(summary = "토큰 리프레시")
  public Token refresh(HttpServletRequest req) {
    var authorization = req.getHeader("authorization");
    var refreshToken = authorization.replace("Bearer ", "");

    return refreshService.refresh(refreshToken);
  }
}
