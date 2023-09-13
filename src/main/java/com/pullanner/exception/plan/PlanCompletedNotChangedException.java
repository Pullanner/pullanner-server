package com.pullanner.exception.plan;

import com.pullanner.web.ApiResponseCode;

public class PlanCompletedNotChangedException extends RuntimeException {

    public PlanCompletedNotChangedException() {
        super(ApiResponseCode.PLAN_COMPLETED_NOT_CHANGED.getMessage());
    }
}
