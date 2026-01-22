package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.MissingArgumentException;

public class InvalidSignedRequestException extends MissingArgumentException {
  public InvalidSignedRequestException() {
    super("signed_request is invalid.");
  }
}
