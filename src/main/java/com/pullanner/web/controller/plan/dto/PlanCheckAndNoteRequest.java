package com.pullanner.web.controller.plan.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@NoArgsConstructor
public class PlanCheckAndNoteRequest {

    private List<PlanWorkoutCheckRequest> workouts;

    @Length(max = 300)
    private String note;
}
