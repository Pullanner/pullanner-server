package com.pullanner.exception.token;

import com.pullanner.web.ApiResponseCode;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super(ApiResponseCode.TOKEN_INVALID.getMessage());
    }
}
