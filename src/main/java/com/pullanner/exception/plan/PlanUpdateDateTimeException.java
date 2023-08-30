package com.pullanner.exception.plan;

import com.pullanner.web.ApiResponseCode;

public class PlanUpdateDateTimeException extends RuntimeException {

    public PlanUpdateDateTimeException() {
        super(ApiResponseCode.PLAN_UPDATE_DATETIME_INVALID.getMessage());
    }
}
