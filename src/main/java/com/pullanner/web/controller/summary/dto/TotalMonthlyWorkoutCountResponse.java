package com.pullanner.web.controller.summary.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TotalMonthlyWorkoutCountResponse {

    private Map<String, List<MonthlyWorkoutCountResponse>> data;

    public static TotalMonthlyWorkoutCountResponse from(Map<String, Map<String, Integer>> totalCountByWorkoutNameForPeriod) {
        Map<String, List<MonthlyWorkoutCountResponse>> data = new LinkedHashMap<>();
        for (String workoutName : totalCountByWorkoutNameForPeriod.keySet()) {
            List<MonthlyWorkoutCountResponse> monthlyWorkoutCountResponses = new ArrayList<>();
            Map<String, Integer> totalCountForPeriod = totalCountByWorkoutNameForPeriod.get(workoutName);
            for (String monthName : totalCountForPeriod.keySet()) {
                monthlyWorkoutCountResponses.add(MonthlyWorkoutCountResponse.of(monthName, totalCountForPeriod.get(monthName)));
            }

            data.put(workoutName, monthlyWorkoutCountResponses);
        }

        return new TotalMonthlyWorkoutCountResponse(data);
    }
}
