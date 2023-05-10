package io.itmca.lifepuzzle.domain.register.endpoint;

import io.itmca.lifepuzzle.domain.register.service.WithdrawService;
import io.itmca.lifepuzzle.domain.user.CurrentUser;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원탈퇴")
public class WithdrawEndpoint {

  private final WithdrawService withdrawService;

  @DeleteMapping("/users/{id}")
  @Operation(summary = "회원탈퇴")
  public void withdraw(@PathVariable("id") Long id,
                       @Parameter(hidden = true) @CurrentUser User user,
                       @RequestBody String socialToken) {
    if (id != user.getUserNo()) {
    }

    withdrawService.withdraw(user, socialToken);
  }
}
