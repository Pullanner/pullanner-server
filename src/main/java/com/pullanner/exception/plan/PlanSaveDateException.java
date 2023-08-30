package com.pullanner.exception.plan;

import com.pullanner.web.ApiResponseCode;

public class PlanSaveDateException extends RuntimeException {
    public PlanSaveDateException() {
        super(ApiResponseCode.PLAN_SAVE_DATE_INVALID.getMessage());
    }
}
