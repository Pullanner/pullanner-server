package com.pullanner.web.controller.plan.dto;

import com.pullanner.domain.plan.PlanWorkout;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PlanWorkoutResponse {

    private Integer count;
    private Integer set;
    private Boolean done;
    private Integer step;

    @Builder
    public PlanWorkoutResponse(Integer count, Integer set, boolean done, Integer step) {
        this.count = count;
        this.set = set;
        this.done = done;
        this.step = step;
    }

    public static PlanWorkoutResponse from(PlanWorkout planWorkout) {
        return PlanWorkoutResponse.builder()
                .count(planWorkout.getCountPerSet())
                .set(planWorkout.getSetCount())
                .done(planWorkout.getDone())
                .step(planWorkout.getIdOfWorkout())
                .build();
    }
}
