package com.pullanner.exception.plan;

import com.pullanner.web.ApiResponseCode;

public class InvalidPlanSaveRequestException extends RuntimeException {

    public InvalidPlanSaveRequestException() {
        super(ApiResponseCode.PLAN_INVALID_SAVE_REQUEST.getMessage());
    }
}
