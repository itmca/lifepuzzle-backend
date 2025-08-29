package io.itmca.lifepuzzle.global.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

  @ExceptionHandler(AuthException.class)
  public ResponseEntity<Void> handleUnAuthorizedException(AuthException e) {
    log.debug("Auth exception occurred: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Void> handleNotFoundException(NotFoundException e) {
    log.debug("Not found exception occurred: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity<Void> handleAlreadyExistsException(AlreadyExistsException e) {
    log.debug("Already exists exception occurred: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }

  @ExceptionHandler(ServerExecutionFailException.class)
  public ResponseEntity<Void> handleServerExecutionFailException(ServerExecutionFailException e) {
    log.info("Server execution fail exception occurred: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }

  @ExceptionHandler(MissingArgumentException.class)
  public ResponseEntity<Void> handleIllegalArgumentException(MissingArgumentException e) {
    log.debug("Missing argument exception occurred: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Void> handleAccessDeniedException(AccessDeniedException e) {
    log.debug("Access denied exception occurred: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

  @ExceptionHandler(ExpiredException.class)
  public ResponseEntity<Void> handleExpiredException(ExpiredException e) {
    log.debug("Expired exception occurred: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

  @ExceptionHandler(ExternalApiException.class)
  public ResponseEntity<Void> handleExternalApiException(ExternalApiException e, HttpServletRequest request) {
    log.info("External API exception at {} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  // ===== Validation and Request Processing Exceptions =====
  
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Void> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
    log.warn("Validation failed at {} {}: {}", request.getMethod(), request.getRequestURI(), e.getBindingResult().getFieldError());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<Void> handleBindException(BindException e, HttpServletRequest request) {
    log.warn("Bind exception at {} {}: {}", request.getMethod(), request.getRequestURI(), e.getBindingResult().getFieldError());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Void> handleMissingParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
    log.warn("Missing parameter at {} {}: {} ({})", request.getMethod(), request.getRequestURI(), e.getParameterName(), e.getParameterType());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Void> handleTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
    log.warn("Type mismatch at {} {}: parameter '{}' should be {}", request.getMethod(), request.getRequestURI(), e.getName(), e.getRequiredType());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<Void> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletRequest request) {
    log.warn("File size exceeded at {} {}: max size is {}", request.getMethod(), request.getRequestURI(), e.getMaxUploadSize());
    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
  }

  // ===== HTTP and Routing Exceptions =====
  
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
    log.warn("No handler found for {} {}", e.getHttpMethod(), e.getRequestURL());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<Void> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
    log.warn("Method not supported at {}: {} (supported: {})", request.getRequestURI(), e.getMethod(), e.getSupportedMethods());
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
  }

  // ===== General Exception Handlers =====
  
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
    log.error("Unexpected runtime exception at {} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Void> handleGenericException(Exception e, HttpServletRequest request) {
    log.error("Unexpected exception at {} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }
}
