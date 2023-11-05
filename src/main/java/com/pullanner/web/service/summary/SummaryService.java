package com.pullanner.web.service.summary;

import com.pullanner.domain.plan.Plan;
import com.pullanner.domain.plan.PlanRepository;
import com.pullanner.domain.plan.PlanWorkout;
import com.pullanner.domain.workout.WorkoutEnum;
import com.pullanner.web.controller.summary.dto.TotalWorkoutCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SummaryService {

    private final PlanRepository planRepository;

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
}
