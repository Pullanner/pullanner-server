package com.pullanner.web.controller.user.dto;

import com.pullanner.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserWorkoutResponse {

    private final List<Integer> workouts;

    @Builder
    private UserWorkoutResponse(List<Integer> workouts) {
        this.workouts = workouts;
    }

    public static UserWorkoutResponse from(User user) {
        return UserWorkoutResponse.builder()
                .workouts(user.getIdsOfWorkout())
                .build();
    }
}
