package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.AuthException;

public class UserNotFoundException extends AuthException {

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
