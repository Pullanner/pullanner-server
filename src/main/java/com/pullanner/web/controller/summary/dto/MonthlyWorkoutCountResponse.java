package com.pullanner.web.controller.summary.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MonthlyWorkoutCountResponse {

    private String month;
    private Integer totalCount;

    public static MonthlyWorkoutCountResponse of(String month, Integer totalCount) {
        return new MonthlyWorkoutCountResponse(month, totalCount);
    }
}
