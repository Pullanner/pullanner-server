package com.pullanner.domain.plan;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    @EntityGraph(value = "PlanWithWriterAndWorkouts")
    Optional<Plan> findWithWriterAndWorkoutsById(Long id);

    @EntityGraph(value = "PlanWithWorkouts")
    Optional<Plan> findWithWorkoutsById(Long id);
}
