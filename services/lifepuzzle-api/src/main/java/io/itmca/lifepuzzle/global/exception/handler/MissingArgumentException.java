package io.itmca.lifepuzzle.global.exception.handler;

public abstract class MissingArgumentException extends RuntimeException {
  public MissingArgumentException(String message) {
    super(message);
  }
}