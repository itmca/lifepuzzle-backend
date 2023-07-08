package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.AuthException;

public class TokenTypeMismatchException extends AuthException {

  private TokenTypeMismatchException(String msg) {
    super(msg);
  }

  public static TokenTypeMismatchException refreshTokenExpected(String token) {
    return new TokenTypeMismatchException(
        String.format("Token: %s is not a refresh token.", token));
  }

  public static TokenTypeMismatchException accessTokenExpected(String token) {
    return new TokenTypeMismatchException(
        String.format("Token: %s is not an access token.", token));
  }

}
