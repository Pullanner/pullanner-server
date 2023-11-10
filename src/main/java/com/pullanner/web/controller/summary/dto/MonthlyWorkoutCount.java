package com.pullanner.web.controller.summary.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MonthlyWorkoutCount {

    private String month;
    private Integer totalCount;

    public static MonthlyWorkoutCount of(String month, Integer totalCount) {
        return new MonthlyWorkoutCount(month, totalCount);
    }
}
