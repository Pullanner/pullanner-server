package com.pullanner.web.controller.summary.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutCountResponse {

    private String workout;
    private Integer totalCount;

    public static WorkoutCountResponse of(String workoutName, Integer totalCount) {
        return new WorkoutCountResponse(workoutName, totalCount);
    }
}
