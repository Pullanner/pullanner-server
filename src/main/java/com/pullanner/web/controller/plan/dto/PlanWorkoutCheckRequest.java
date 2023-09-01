package com.pullanner.web.controller.plan.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlanWorkoutCheckRequest {

    private int step;
    private Boolean done;
}
