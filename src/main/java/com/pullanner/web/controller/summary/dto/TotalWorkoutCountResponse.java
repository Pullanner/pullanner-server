package com.pullanner.web.controller.summary.dto;

import com.pullanner.domain.workout.WorkoutEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TotalWorkoutCountResponse {

    private List<WorkoutCountResponse> totalCountByWorkout;

    public static TotalWorkoutCountResponse from(Map<String, Integer> totalCountByWorkout) {
        List<WorkoutCountResponse> workoutCountResponses = new ArrayList<>();
        List<String> workoutNames = WorkoutEnum.findAllWorkoutNames();
        for (String workoutName : workoutNames) {
            workoutCountResponses.add(WorkoutCountResponse.of(workoutName, totalCountByWorkout.get(workoutName)));
        }

        return new TotalWorkoutCountResponse(workoutCountResponses);
    }
}