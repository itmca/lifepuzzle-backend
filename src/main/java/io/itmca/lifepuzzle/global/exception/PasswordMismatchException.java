package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.AuthException;

public class PasswordMismatchException extends AuthException {

    public PasswordMismatchException() {
        super("Password does not match");
    }

}
