package io.itmca.lifepuzzle.global.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity handleUnAuthorizedException(AuthException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
