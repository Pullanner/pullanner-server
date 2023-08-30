package com.pullanner.exception.token;

import com.pullanner.web.ApiResponseCode;

public class HackedTokenException extends RuntimeException {

    public HackedTokenException() {
        super(ApiResponseCode.TOKEN_HACKED.getMessage());
    }
}
