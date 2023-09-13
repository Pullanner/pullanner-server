package com.pullanner.exception.plan;

import com.pullanner.web.ApiResponseCode;

public class PlanWorkoutDoneException extends RuntimeException {

    public PlanWorkoutDoneException() {
        super(ApiResponseCode.PLAN_WORKOUT_DONE.getMessage());
    }
}
