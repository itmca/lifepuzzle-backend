package io.itmca.lifepuzzle.global.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

  @ExceptionHandler(AuthException.class)
  public ResponseEntity handleUnAuthorizedException(AuthException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity handleNotFoundException(NotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity handleAlreadyExistsException(AlreadyExistsException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }

  @ExceptionHandler(ServerExecutionFailException.class)
  public ResponseEntity handleServerExecutionFailException(ServerExecutionFailException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }

  @ExceptionHandler(MissingArgumentException.class)
  public ResponseEntity handleIllegalArgumentException(MissingArgumentException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity handleAccessDeniedException(AccessDeniedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

  @ExceptionHandler(ExpiredException.class)
  public ResponseEntity handleExpiredException(ExpiredException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

  @ExceptionHandler(ExternalApiException.class)
  public ResponseEntity handleExternalApiException(ExternalApiException e) {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
