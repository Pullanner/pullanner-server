package com.pullanner.web.controller.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserWorkoutSaveOrUpdateRequest {

    @Size(min = 1, max = 8)
    private List<Integer> workouts;
}
