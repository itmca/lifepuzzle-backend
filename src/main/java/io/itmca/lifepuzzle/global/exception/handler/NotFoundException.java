package io.itmca.lifepuzzle.global.exception.handler;

public abstract class NotFoundException extends RuntimeException{
    public NotFoundException(String msg) {
        super(msg);
    }
}
