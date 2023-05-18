package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.AuthException;

public class TokenTypeMismatchException extends AuthException {

  public TokenTypeMismatchException(String tokenType) {
    super(String.format("Token type does not match - tokenType: %s", tokenType));
  }

}
