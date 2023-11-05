package com.pullanner.web.service.summary;

import com.pullanner.domain.plan.Plan;
import com.pullanner.domain.plan.PlanCompletedTimeEnum;
import com.pullanner.domain.plan.PlanRepository;
import com.pullanner.domain.plan.PlanWorkout;
import com.pullanner.domain.workout.WorkoutEnum;
import com.pullanner.web.controller.summary.dto.CompletedPlanCountByTime;
import com.pullanner.web.controller.summary.dto.TotalMonthlyWorkoutCountResponse;
import com.pullanner.web.controller.summary.dto.TotalWorkoutCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
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

        List<Plan> plans = planRepository.findAllByUserIdBetweenPeriodOfPlanDate(userId, startDate, endDate);
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

    @Transactional(readOnly = true)
    public CompletedPlanCountByTime getCompletedPlanCountByTime(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Integer> completedPlanCountByTimeThisMonth = createCompletedPlanCountByTime();
        Map<String, Integer> completedPlanCountByTimePreviousMonth = createCompletedPlanCountByTime();

        Month thisMonth = endDate.getMonth();
        Month previousMonth = startDate.getMonth();

        List<Plan> plans = planRepository.findAllByUserIdBetweenPeriodOfCompletedDate(userId, startDate, endDate);
        for (Plan plan : plans) {
            if (plan.isCompleted()) {
                if (plan.matchGivenMonth(thisMonth)) {
                    String planCompletedTime = plan.getPlanCompletedTime();
                    completedPlanCountByTimeThisMonth.put(planCompletedTime, completedPlanCountByTimeThisMonth.get(planCompletedTime) + 1);
                } else if (plan.matchGivenMonth(previousMonth)) {
                    String planCompletedTime = plan.getPlanCompletedTime();
                    completedPlanCountByTimePreviousMonth.put(planCompletedTime, completedPlanCountByTimePreviousMonth.get(planCompletedTime) + 1);
                } else {
                    throw new IllegalStateException();
                }
            }
        }

        return CompletedPlanCountByTime.of(completedPlanCountByTimeThisMonth, completedPlanCountByTimePreviousMonth);
    }

    private Map<String, Integer> createCompletedPlanCountByTime() {
        Map<String, Integer> completedPlanCountByTimeThisMonth = new LinkedHashMap<>();
        List<String> times = PlanCompletedTimeEnum.findTimes();
        for (String time : times) {
            completedPlanCountByTimeThisMonth.put(time, 0);
        }

        return completedPlanCountByTimeThisMonth;
    }
}
