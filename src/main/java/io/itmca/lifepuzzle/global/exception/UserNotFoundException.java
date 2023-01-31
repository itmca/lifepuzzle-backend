package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.AuthException;
import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;
import org.hibernate.annotations.NotFound;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(Long userNo) {
        super(String.format("User is not found - userNo: %d", userNo));
    }
}
