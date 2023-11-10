package com.pullanner.web.controller.summary.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TotalMonthlyWorkoutCountResponse {

    private List<MonthlyWorkoutCountResponse> workoutCountPerMonth;

    public static TotalMonthlyWorkoutCountResponse from(Map<Integer, Map<String, Integer>> totalCountByWorkoutNameForPeriod) {
        List<MonthlyWorkoutCountResponse> workoutCountPerMonth = new ArrayList<>();
        for (Integer workoutId : totalCountByWorkoutNameForPeriod.keySet()) {
            Map<String, Integer> totalCountForPeriod = totalCountByWorkoutNameForPeriod.get(workoutId);
            workoutCountPerMonth.add(MonthlyWorkoutCountResponse.of(workoutId, totalCountForPeriod));
        }

        return new TotalMonthlyWorkoutCountResponse(workoutCountPerMonth);
    }
}
