package com.pullanner.web.controller.summary.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MonthlyWorkoutCountResponse {

    private Integer step;
    private List<MonthlyWorkoutCount> data;

    public static MonthlyWorkoutCountResponse of(Integer step, Map<String, Integer> totalCountByMonth) {
        List<MonthlyWorkoutCount> workoutCounts = new ArrayList<>();
        for (String month : totalCountByMonth.keySet()) {
            workoutCounts.add(MonthlyWorkoutCount.of(month, totalCountByMonth.get(month)));
        }

        return new MonthlyWorkoutCountResponse(step, workoutCounts);
    }
}
