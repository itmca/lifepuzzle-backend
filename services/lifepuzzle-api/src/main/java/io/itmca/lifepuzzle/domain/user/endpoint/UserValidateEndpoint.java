package io.itmca.lifepuzzle.domain.user.endpoint;

import io.itmca.lifepuzzle.domain.user.endpoint.response.IdDuplicateCheckResponse;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
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

  @GetMapping("/v1/users/dupcheck/id")
  @Operation(summary = "아이디 중복 체크")
  public IdDuplicateCheckResponse checkId(@RequestParam("id") String id) {
    boolean duplicated = userQueryService.existsByLoginId(id);
    return new IdDuplicateCheckResponse(duplicated);
  }
}