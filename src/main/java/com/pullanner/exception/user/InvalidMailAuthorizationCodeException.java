package com.pullanner.exception.user;

import com.pullanner.web.ApiResponseCode;

public class InvalidMailAuthorizationCodeException extends RuntimeException {

    public InvalidMailAuthorizationCodeException() {
        super(ApiResponseCode.USER_INVALID_MAIL_AUTHORIZATION_CODE.getMessage());
    }
}
