package com.pullanner.domain.plan;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    @Query(value = "select p from Plan p where p.id = :planId and p.writer.id = :userId")
    Optional<Plan> findByPlanIdAndUserId(@Param("planId") Long planId, @Param("userId") Long userId);

    @Query(value = "select p from Plan p where p.writer.id = :userId and p.completed = true order by p.completedDate desc")
    List<Plan> findCompletedPlansByUserId(@Param("userId") Long userId);

    @Query(value = "select p from Plan p join fetch p.planWorkouts pw where p.planDate between :startDate and :endDate and p.writer.id = :userId")
    List<Plan> findAllWithPlanWorkoutsForPeriodByUserId(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("userId") Long userId);

    @EntityGraph(value = "PlanWithPlanWorkouts")
    Optional<Plan> findWithPlanWorkoutsById(Long id);

    @Query(value = "select p from Plan p join fetch p.planWorkouts pw where p.writer.id = :userId")
    List<Plan> findAllByUserId(@Param("userId") Long userId);

    @Query(value = "select p from Plan p join fetch p.planWorkouts pw where p.writer.id = :userId and p.planDate between :startDate and :endDate")
    List<Plan> findAllByUserIdBetweenPeriodOfPlanDate(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "select p from Plan p join fetch p.planWorkouts pw where p.writer.id = :userId and p.completedDate between :startDate and :endDate")
    List<Plan> findAllByUserIdBetweenPeriodOfCompletedDate(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
