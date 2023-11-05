package com.pullanner.domain.workout;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum WorkoutEnum {

    HANGING(1, "Hanging"),
    JUMPING_PULL_UP(2, "Jumping Pull-up"),
    BAND_PULL_UP(3, "Band Pull-up"),
    CHIN_UP(4, "Chin-up"),
    PULL_UP(5, "Pull-up"),
    CHEST_TO_BAR_PULL_UP(6, "Chest to Bar Pull-up"),
    ARCHER_PULL_UP(7, "Archer Pull-up"),
    MUSCLE_UP(8, "Muscle up");

    private static final Map<Integer, String> workoutNameById = Stream.of(values())
            .collect(Collectors.toUnmodifiableMap(WorkoutEnum::getId, WorkoutEnum::getName));

    private static final List<String> workoutNames = Stream.of(values())
            .map(WorkoutEnum::getName)
            .toList();

    private final int id;
    private final String name;

    public static List<String> findAllWorkoutNames() {
        return workoutNames;
    }

    public static String findWorkoutNameById(int id) {
        return workoutNameById.get(id);
    }
}
