package com.pullanner.web.controller.plan.dto;

import com.pullanner.domain.plan.PlanType;
import com.pullanner.web.validation.PlanDateValid;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PlanSaveOrUpdateRequest {

    @PlanDateValid
    private LocalDateTime planDateTime;

    @Length(min = 1, max = 20)
    private String planName;

    private PlanType planType;

    private List<PlanWorkoutRequest> workouts;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
    private String mainColor;

    public Map<Integer, PlanWorkoutRequest> getIdsOfWorkouts() {
        return workouts.stream()
                .collect(Collectors.toMap(
                        PlanWorkoutRequest::getStep,
                        planWorkoutRequest -> planWorkoutRequest
                ));
    }
}
