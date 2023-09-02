package com.pullanner.web.controller.plan.dto;

import com.pullanner.domain.plan.Plan;
import com.pullanner.domain.plan.PlanType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlanResponse {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime planDateTime;
    private String planName;
    private PlanType planType;
    private List<PlanWorkoutResponse> workouts;
    private Integer progress;
    private String note;
    private Integer mainWorkoutStep;

    public static PlanResponse of(Plan plan, List<PlanWorkoutResponse> workouts,
                                  Integer progress, Integer mainWorkoutStep) {
        return PlanResponse.builder()
                .id(plan.getId())
                .createdAt(plan.getCreatedDate())
                .updatedAt(plan.getModifiedDate())
                .planDateTime(plan.getPlanDate())
                .planName(plan.getName())
                .planType(plan.getPlanType())
                .workouts(workouts)
                .progress(progress)
                .note(plan.getNote())
                .mainWorkoutStep(mainWorkoutStep)
                .build();
    }
}
