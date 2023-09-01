package com.pullanner.exception.user;

public class UserNotFoundedException extends RuntimeException {

    public UserNotFoundedException(String message) {
        super(message);
    }
}
