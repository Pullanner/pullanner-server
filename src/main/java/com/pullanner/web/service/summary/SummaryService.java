package com.pullanner.web.service.summary;

import com.pullanner.domain.plan.Plan;
import com.pullanner.domain.plan.PlanRepository;
import com.pullanner.domain.plan.PlanWorkout;
import com.pullanner.domain.workout.WorkoutEnum;
import com.pullanner.web.controller.summary.dto.TotalMonthlyWorkoutCountResponse;
import com.pullanner.web.controller.summary.dto.TotalWorkoutCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class SummaryService {

    private final PlanRepository planRepository;

    @Transactional(readOnly = true)
    public TotalWorkoutCountResponse getTotalCountByWorkout(Long userId) {
        Map<String, Integer> totalCountByWorkout = createTotalCountByWorkout();

        List<Plan> plans = planRepository.findAllByUserId(userId);
        for (Plan plan : plans) {
            List<PlanWorkout> planWorkouts = plan.getPlanWorkouts();
            for (PlanWorkout planWorkout : planWorkouts) {
                if (planWorkout.getDone()) {
                    String workoutName = WorkoutEnum.findWorkoutNameById(planWorkout.getIdOfWorkout());
                    int totalCount = planWorkout.getTotalCount();
                    totalCountByWorkout.put(workoutName, totalCountByWorkout.get(workoutName) + totalCount);
                }
            }
        }

        return TotalWorkoutCountResponse.from(totalCountByWorkout);
    }

    private Map<String, Integer> createTotalCountByWorkout() {
        Map<String, Integer> totalCountByWorkout = new HashMap<>();
        List<String> workoutNames = WorkoutEnum.findAllWorkoutNames();
        for (String workoutName : workoutNames) {
            totalCountByWorkout.put(workoutName, 0);
        }

        return totalCountByWorkout;
    }

    @Transactional(readOnly = true)
    public TotalMonthlyWorkoutCountResponse getTotalCountByWorkoutNameForPeriod(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<String> monthNamesForPeriod = getMonthNamesForPeriod(startDate);

        Map<String, Map<String, Integer>> totalCountByWorkoutNameForPeriod = createTotalCountByWorkoutNameForPeriod(monthNamesForPeriod);

        List<Plan> plans = planRepository.findAllByUserIdBetweenPeriod(userId, startDate, endDate);
        for (Plan plan : plans) {
            List<PlanWorkout> planWorkouts = plan.getPlanWorkouts();
            for (PlanWorkout planWorkout : planWorkouts) {
                if (planWorkout.getDone()) {
                    String workoutName = WorkoutEnum.findWorkoutNameById(planWorkout.getIdOfWorkout());
                    String monthName = getSubStringOfMonthName(planWorkout.getModifiedDate());
                    int totalCount = planWorkout.getTotalCount();

                    Map<String, Integer> totalCountByMonth = totalCountByWorkoutNameForPeriod.get(workoutName);
                    totalCountByMonth.put(monthName, totalCountByMonth.get(monthName) + totalCount);
                }
            }
        }

        return TotalMonthlyWorkoutCountResponse.from(totalCountByWorkoutNameForPeriod);
    }

    private static Map<String, Map<String, Integer>> createTotalCountByWorkoutNameForPeriod(List<String> monthNamesForPeriod) {
        Map<String, Map<String, Integer>> totalCountByWorkoutNameForPeriod = new LinkedHashMap<>();
        List<String> workoutNames = WorkoutEnum.findAllWorkoutNames();
        for (String workoutName : workoutNames) {
            Map<String, Integer> totalCountByMonth = new LinkedHashMap<>();

            for (String monthName : monthNamesForPeriod) {
                totalCountByMonth.put(monthName, 0);
            }

            totalCountByWorkoutNameForPeriod.put(workoutName, totalCountByMonth);
        }
        return totalCountByWorkoutNameForPeriod;
    }

    private List<String> getMonthNamesForPeriod(LocalDateTime startDate) {
        List<String> monthNamesForPeriod = new ArrayList<>();
        monthNamesForPeriod.add(getSubStringOfMonthName(startDate));
        monthNamesForPeriod.add(getSubStringOfMonthName(startDate.plusMonths(1)));
        monthNamesForPeriod.add(getSubStringOfMonthName(startDate.plusMonths(2)));
        monthNamesForPeriod.add(getSubStringOfMonthName(startDate.plusMonths(3)));
        monthNamesForPeriod.add(getSubStringOfMonthName(startDate.plusMonths(4)));
        monthNamesForPeriod.add(getSubStringOfMonthName(startDate.plusMonths(5)));

        return monthNamesForPeriod;
    }

    private String getSubStringOfMonthName(LocalDateTime date) {
        return date.getMonth().name().substring(0, 3);
    }
}
