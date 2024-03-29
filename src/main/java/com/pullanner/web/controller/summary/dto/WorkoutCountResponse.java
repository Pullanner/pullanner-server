package com.pullanner.web.controller.summary.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkoutCountResponse {

    private Integer step;
    private Integer totalCount;

    public static WorkoutCountResponse of(Integer step, Integer totalCount) {
        return new WorkoutCountResponse(step, totalCount);
    }
}
