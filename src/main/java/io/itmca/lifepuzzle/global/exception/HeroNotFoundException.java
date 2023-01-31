package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.AuthException;
import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;

public class HeroNotFoundException extends NotFoundException {

    public HeroNotFoundException(Long heroNo) {
        super(String.format("Hero is not found - heroNo: %d", heroNo));
    }
}
