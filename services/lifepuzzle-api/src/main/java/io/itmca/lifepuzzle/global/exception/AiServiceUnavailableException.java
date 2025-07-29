package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.ExternalApiException;

public class AiServiceUnavailableException extends ExternalApiException {
  public AiServiceUnavailableException(String serviceName) {
    super(String.format("%s service not unavailable because of api call fail", serviceName));
  }
}
