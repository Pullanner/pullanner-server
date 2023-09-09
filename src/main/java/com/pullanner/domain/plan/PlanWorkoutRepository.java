package com.pullanner.domain.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlanWorkoutRepository extends JpaRepository<PlanWorkout, Long> {

    @Query(value = "select pw from PlanWorkout pw where pw.plan.id in :planIds")
    List<PlanWorkout> findByPlanIds(@Param("planIds") List<Long> planIds);
}
