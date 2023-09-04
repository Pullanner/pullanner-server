package com.pullanner.exception.user;

import com.pullanner.web.ApiResponseCode;

public class AlreadySentAuthorizationCodeException extends RuntimeException {
    public AlreadySentAuthorizationCodeException() {
        super(ApiResponseCode.USER_ALREADY_SENT_AUTHORIZATION_CODE.getMessage());
    }
}
