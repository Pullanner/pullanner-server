package com.pullanner.domain.user;

public class InvalidMailAuthorizationCodeException extends RuntimeException {

    public InvalidMailAuthorizationCodeException(String message) {
        super(message);
    }
}
