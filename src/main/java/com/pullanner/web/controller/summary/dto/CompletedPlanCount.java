package com.pullanner.web.controller.summary.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CompletedPlanCount {

    private String time;
    private Integer thisMonth;
    private Integer prevMonth;

    public static CompletedPlanCount of(String time, Integer thisMonth, Integer prevMonth) {
        return new CompletedPlanCount(time, thisMonth, prevMonth);
    }
}
