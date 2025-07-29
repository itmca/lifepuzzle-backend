package io.itmca.lifepuzzle.global.exception;

import org.springframework.security.access.AccessDeniedException;

public class HeroAccessDeniedException extends AccessDeniedException {
  public HeroAccessDeniedException() {
    super("Insufficient permissions to execute this action");
  }
}
