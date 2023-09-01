package com.pullanner.exception.plan;

import com.pullanner.web.ApiResponseCode;

public class PlanWorkoutNotFoundedException extends RuntimeException {

    public PlanWorkoutNotFoundedException() {
        super(ApiResponseCode.PLAN_WORKOUT_NOT_FOUNDED.getMessage());
    }
}
