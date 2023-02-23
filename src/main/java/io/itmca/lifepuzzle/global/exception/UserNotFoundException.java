package io.itmca.lifepuzzle.global.exception;

import io.itmca.lifepuzzle.global.exception.handler.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(Long userNo) {
        super(String.format("User is not found - userNo: %d", userNo));
    }

    public UserNotFoundException(String userId) {
        super(String.format("User is not found - userId: %d", userId));
    }

}
