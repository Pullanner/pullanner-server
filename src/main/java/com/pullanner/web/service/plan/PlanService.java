package com.pullanner.web.service.plan;

import com.pullanner.domain.plan.Plan;
import com.pullanner.domain.plan.PlanRepository;
import com.pullanner.domain.plan.PlanWorkout;
import com.pullanner.domain.plan.PlanWorkoutRepository;
import com.pullanner.domain.user.User;
import com.pullanner.domain.user.UserRepository;
import com.pullanner.domain.workout.WorkoutRepository;
import com.pullanner.web.controller.plan.dto.PlanSaveOrUpdateRequest;
import com.pullanner.web.controller.plan.dto.PlanWorkoutRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final PlanWorkoutRepository planWorkoutRepository;

    @Transactional
    public void save(Long userId, PlanSaveOrUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("식별 번호가 " + userId + "에 해당되는 사용자가 없습니다.")
        );

        Plan plan = Plan.builder()
                .writer(user)
                .name(request.getPlanName())
                .planType(request.getPlanType())
                .planDate(request.getPlanDateTime())
                .mainColor(request.getMainColor())
                .build();

        user.addPlan(plan);

        Map<Integer, PlanWorkoutRequest> planWorkoutRequestByWorkoutId = getIdsOfWorkouts(request);

        List<PlanWorkout> planWorkouts = workoutRepository.findAllByIdIn(planWorkoutRequestByWorkoutId.keySet())
                .stream()
                .map(workout -> {
                    PlanWorkoutRequest planWorkoutRequest = planWorkoutRequestByWorkoutId.get(workout.getId());
                    PlanWorkout planWorkout = PlanWorkout.builder()
                            .plan(plan)
                            .workout(workout)
                            .set(planWorkoutRequest.getSet())
                            .countPerSet(planWorkoutRequest.getCount())
                            .build();

                    plan.addPlanWorkout(planWorkout);
                    workout.addPlanWorkout(planWorkout);

                    return planWorkout;
                })
                .toList();

        planRepository.save(plan);
        planWorkoutRepository.saveAll(planWorkouts);
    }

    private Map<Integer, PlanWorkoutRequest> getIdsOfWorkouts(PlanSaveOrUpdateRequest request) {
        return request.getWorkouts()
                .stream()
                .collect(Collectors.toMap(
                        PlanWorkoutRequest::getStep,
                        planWorkoutRequest -> planWorkoutRequest
                ));
    }
}
