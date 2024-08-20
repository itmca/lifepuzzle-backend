package io.itmca.lifepuzzle.domain;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthEndpoint {

  @Operation(hidden = true)
  @RequestMapping({"/", "hc"})
  public String healthCheck() {
    return "ok";
  }

  @Operation(hidden = true)
  @RequestMapping("error-test")
  public String errorTest() {
    throw new RuntimeException("예외 테스트 입니다.");
  }
}
