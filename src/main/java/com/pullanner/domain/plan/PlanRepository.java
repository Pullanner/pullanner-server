package com.pullanner.domain.plan;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    @Query(value = "select p from Plan p where p.id = :planId and p.writer = :userId")
    Optional<Plan> findByPlanIdAndUserId(@Param("planId") Long planId, @Param("userId") Long userId);

    @EntityGraph(value = "PlanWithWriter")
    Optional<Plan> findWithWriterById(Long id);

    @EntityGraph(value = "PlanWithWriterAndWorkouts")
    Optional<Plan> findWithWriterAndWorkoutsById(Long id);

    @EntityGraph(value = "PlanWithWorkouts")
    Optional<Plan> findWithWorkoutsById(Long id);
}
