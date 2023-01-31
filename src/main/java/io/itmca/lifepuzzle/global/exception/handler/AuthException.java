package io.itmca.lifepuzzle.global.exception.handler;

public abstract class AuthException extends RuntimeException {

    public AuthException() {
    }

    public AuthException(String msg) {
        super(msg);
    }

}
