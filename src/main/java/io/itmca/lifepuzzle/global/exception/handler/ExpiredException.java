package io.itmca.lifepuzzle.global.exception.handler;

public abstract class ExpiredException extends RuntimeException {
  public ExpiredException(String msg) {
    super(msg);
  }
}
