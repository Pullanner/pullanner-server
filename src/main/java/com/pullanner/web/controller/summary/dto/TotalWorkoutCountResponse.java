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

    public static TotalWorkoutCountResponse from(Map<Integer, Integer> totalCountByWorkout) {
        List<WorkoutCountResponse> workoutCountResponses = new ArrayList<>();
        List<Integer> workoutIds = WorkoutEnum.findAllWorkoutIds();
        for (Integer workoutId : workoutIds) {
            workoutCountResponses.add(WorkoutCountResponse.of(workoutId, totalCountByWorkout.get(workoutId)));
        }

        return new TotalWorkoutCountResponse(workoutCountResponses);
    }
}
