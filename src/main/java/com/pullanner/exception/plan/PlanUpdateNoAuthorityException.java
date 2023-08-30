package com.pullanner.exception.plan;

import com.pullanner.web.ApiResponseCode;

public class PlanUpdateNoAuthorityException extends RuntimeException {

    public PlanUpdateNoAuthorityException() {
        super(ApiResponseCode.PLAN_UPDATE_NO_AUTHORITY.getMessage());
    }
}
