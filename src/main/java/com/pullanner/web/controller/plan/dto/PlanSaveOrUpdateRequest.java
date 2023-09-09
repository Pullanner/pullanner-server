package com.pullanner.web.controller.plan.dto;

import com.pullanner.domain.plan.PlanType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PlanSaveOrUpdateRequest {

    private LocalDateTime planDateTime;

    @Length(min = 1, max = 20)
    private String planName;

    private PlanType planType;

    private List<PlanWorkoutRequest> workouts;

    public Set<Integer> getWorkoutIdSetOfPlanSaveRequest() {
        return workouts.stream()
                .map(PlanWorkoutRequest::getStep)
                .collect(Collectors.toSet());
    }
}
