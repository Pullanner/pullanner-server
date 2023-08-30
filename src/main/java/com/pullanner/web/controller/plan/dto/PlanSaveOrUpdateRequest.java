package com.pullanner.web.controller.plan.dto;

import com.pullanner.domain.plan.PlanType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PlanSaveOrUpdateRequest {

    private LocalDateTime planDateTime;

    @Length(min = 1, max = 20)
    private String planName;

    private PlanType planType;

    private List<PlanWorkoutRequest> workouts;
}
