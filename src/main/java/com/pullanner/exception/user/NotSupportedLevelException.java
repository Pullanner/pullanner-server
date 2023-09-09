package com.pullanner.exception.user;

import com.pullanner.web.ApiResponseCode;

public class NotSupportedLevelException extends RuntimeException {

    public NotSupportedLevelException() {
        super(ApiResponseCode.USER_LEVEL_NOT_SUPPORTED.getMessage());
    }
}
