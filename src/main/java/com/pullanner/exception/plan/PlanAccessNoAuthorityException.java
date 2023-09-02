package com.pullanner.exception.plan;

import com.pullanner.web.ApiResponseCode;

public class PlanAccessNoAuthorityException extends RuntimeException {

    public PlanAccessNoAuthorityException() {
        super(ApiResponseCode.PLAN_ACCESS_NO_AUTHORITY.getMessage());
    }
}
