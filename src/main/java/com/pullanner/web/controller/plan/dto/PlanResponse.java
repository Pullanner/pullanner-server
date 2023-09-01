package com.pullanner.web.controller.plan.dto;

import com.pullanner.domain.plan.Plan;
import com.pullanner.domain.plan.PlanType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
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

    @Builder
    public PlanResponse(Long id, LocalDateTime createdAt, LocalDateTime updatedAt,
                        LocalDateTime planDateTime, String planName, PlanType planType,
                        List<PlanWorkoutResponse> workouts, Integer progress,
                        String note, Integer mainWorkoutStep) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.planDateTime = planDateTime;
        this.planName = planName;
        this.planType = planType;
        this.workouts = workouts;
        this.progress = progress;
        this.note = note;
        this.mainWorkoutStep = mainWorkoutStep;
    }

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
