package io.itmca.lifepuzzle.global.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity handleUnAuthorizedException(AuthException e) {
        log.error("handleUnAuthorizedException", e);
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
