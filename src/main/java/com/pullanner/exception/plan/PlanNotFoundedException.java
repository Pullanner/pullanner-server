package com.pullanner.exception.plan;

import com.pullanner.web.ApiResponseCode;

public class PlanNotFoundedException extends RuntimeException {

    public PlanNotFoundedException(String message) {
        super(message);
    }
}
