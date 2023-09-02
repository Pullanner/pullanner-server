package com.pullanner.web.controller.plan.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlanResponsesByMonth {

    private Map<LocalDate, List<PlanResponse>> data;

    public static PlanResponsesByMonth from(Map<LocalDate, List<PlanResponse>> data) {
        return PlanResponsesByMonth.builder()
                .data(data)
                .build();
    }
}
