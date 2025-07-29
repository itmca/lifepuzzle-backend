package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.AuthException;

public class RefreshTokenRequiredException extends AuthException {
  public RefreshTokenRequiredException(String token) {
    super(String.format("Token: %s is not a refresh token.", token));
  }
}
