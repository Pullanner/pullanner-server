package com.pullanner.exception.user;

public class InvalidMailAuthorizationCodeException extends RuntimeException {

    public InvalidMailAuthorizationCodeException(String message) {
        super(message);
    }
}
