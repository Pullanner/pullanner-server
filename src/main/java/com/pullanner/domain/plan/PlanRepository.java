package com.pullanner.domain.plan;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    @Query(value = "select p from Plan p where p.id = :planId and p.writer.id = :userId")
    Optional<Plan> findByPlanIdAndUserId(@Param("planId") Long planId, @Param("userId") Long userId);

    @EntityGraph(value = "PlanWithPlanWorkouts")
    Optional<Plan> findWithPlanWorkoutsById(Long id);

    @EntityGraph(value = "PlanWithPlanWorkoutsAndWorkouts")
    Optional<Plan> findWithPlanWorkoutsAndWorkoutsById(Long id);
}
