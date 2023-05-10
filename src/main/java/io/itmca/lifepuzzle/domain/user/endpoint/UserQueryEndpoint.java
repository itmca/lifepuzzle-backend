package io.itmca.lifepuzzle.domain.user.endpoint;

import io.itmca.lifepuzzle.domain.user.endpoint.response.UserQueryDto;
import io.itmca.lifepuzzle.domain.user.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "유저 조회")
public class UserQueryEndpoint {

  private final UserQueryService userQueryService;

  @GetMapping("/users/{id}")
  @Operation(summary = "유저 정보")
  public UserQueryDto getOne(@PathVariable("id") Long id) {
    var user = userQueryService.findByUserNo(id);

    return UserQueryDto.from(user);
  }
}
