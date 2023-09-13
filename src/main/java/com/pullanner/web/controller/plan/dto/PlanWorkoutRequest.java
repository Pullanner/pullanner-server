package com.pullanner.web.controller.plan.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlanWorkoutRequest {

    private int step;
    private int count;
    private int set;
    private Boolean done;
}
