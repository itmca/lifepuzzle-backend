package io.itmca.lifepuzzle.global.exception.handler;

public abstract class AlreadyExistsException extends RuntimeException {
  public AlreadyExistsException(String msg) {
    super(msg);
  }
}
