package com.pullanner.web.controller.plan.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
public class PlanResponsesByMonth {

    private Map<LocalDateTime, List<PlanResponse>> data;
}
