package com.pullanner.domain.plan;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PlanType {
    STRENGTH, MASTER;

    @JsonCreator
    public static PlanType from(String s) {
        return PlanType.valueOf(s.toUpperCase());
    }
}
