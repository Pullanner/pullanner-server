package com.pullanner.web.controller.summary.dto;

import com.pullanner.domain.plan.PlanCompletedTimeEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CompletedPlanCountByTime {

    private List<CompletedPlanCount> completedPlanCountByTime;

    public static CompletedPlanCountByTime of(
            Map<String, Integer> completedPlanCountByTimeThisMonth,
            Map<String, Integer> completedPlanCountByTimePreviousMonth) {
        List<CompletedPlanCount> completedPlanCountByTime = new ArrayList<>();

        List<String> times = PlanCompletedTimeEnum.findTimes();
        for (String time : times) {
            completedPlanCountByTime.add(CompletedPlanCount.of(time, completedPlanCountByTimeThisMonth.get(time), completedPlanCountByTimePreviousMonth.get(time)));
        }

        return new CompletedPlanCountByTime(completedPlanCountByTime);
    }
}
