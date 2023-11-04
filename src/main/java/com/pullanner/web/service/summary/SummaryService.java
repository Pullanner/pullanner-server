package com.pullanner.web.service.summary;

import com.pullanner.domain.plan.Plan;
import com.pullanner.domain.plan.PlanRepository;
import com.pullanner.domain.plan.PlanWorkout;
import com.pullanner.domain.workout.Workout;
import com.pullanner.domain.workout.WorkoutRepository;
import com.pullanner.web.controller.summary.dto.TotalWorkoutCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SummaryService {

    private final WorkoutRepository workoutRepository;
    private final PlanRepository planRepository;

    public TotalWorkoutCountResponse getTotalCountByWorkout(Long userId) {
        List<Workout> workouts = workoutRepository.findAll();
        Map<Integer, String> workoutNameById = createWorkoutNameById(workouts);
        Map<String, Integer> totalCountByWorkout = createTotalCountByWorkout(workouts);

        List<Plan> plans = planRepository.findAllByUserId(userId);
        for (Plan plan : plans) {
            List<PlanWorkout> planWorkouts = plan.getPlanWorkouts();
            for (PlanWorkout planWorkout : planWorkouts) {
                if (planWorkout.getDone()) {
                    String workoutName = workoutNameById.get(planWorkout.getIdOfWorkout());
                    int totalCount = planWorkout.getTotalCount();
                    totalCountByWorkout.put(workoutName, totalCountByWorkout.get(workoutName) + totalCount);
                }
            }
        }

        return TotalWorkoutCountResponse.from(totalCountByWorkout);
    }

    private Map<Integer, String> createWorkoutNameById(List<Workout> workouts) {
        Map<Integer, String> workoutNameById = new HashMap<>();
        for (Workout workout : workouts) {
            workoutNameById.put(workout.getId(), workout.getName());
        }

        return workoutNameById;
    }

    private Map<String, Integer> createTotalCountByWorkout(List<Workout> workouts) {
        Map<String, Integer> totalCountByWorkout = new HashMap<>();
        for (Workout workout : workouts) {
            totalCountByWorkout.put(workout.getName(), 0);
        }

        return totalCountByWorkout;
    }


}
