package io.itmca.lifepuzzle.domain.user.endpoint;

import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "유저 검증")
public class UserValidateEndpoint {

  private final UserQueryService userQueryService;

  @GetMapping(value = {"/user/dupcheck/id", "/v1/users/dupcheck/id"})
  @Operation(summary = "아이디 중복 체크")
  public boolean checkId(@RequestParam("id") String id) {
    try {
      userQueryService.findByLoginId(id);
    } catch (NotFoundException e) {
      return false;
    }

    return true;
  }
}