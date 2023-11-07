package com.pullanner.domain.plan;

import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

@Getter
public enum PlanCompletedTimeEnum {

    MORNING("morning"), AFTER_NOON("afternoon"), EVENING("evening"), NIGHT("night");

    private final String time;

    PlanCompletedTimeEnum(String time) {
        this.time = time;
    }

    private static final List<String> times = Stream.of(values()).map(PlanCompletedTimeEnum::getTime).toList();

    public static List<String> findTimes() {
        return times;
    }
}
