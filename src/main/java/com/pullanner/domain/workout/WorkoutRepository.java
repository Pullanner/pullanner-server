package com.pullanner.domain.workout;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<Workout> findAllByIdIn(List<Integer> ids);
}