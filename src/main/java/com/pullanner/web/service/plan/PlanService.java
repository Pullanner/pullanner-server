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

    private final PlanValidationService planValidationService;

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final PlanWorkoutRepository planWorkoutRepository;

    @Transactional
    public void save(Long userId, PlanSaveOrUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException("식별 번호가 " + userId + "에 해당되는 사용자가 없습니다.")
        );

        planValidationService.validatePlanSaveDate(request);

        Plan plan = Plan.builder()
                .writer(user)
                .name(request.getPlanName())
                .planType(request.getPlanType())
                .planDate(request.getPlanDateTime())
                .build();

        user.addPlan(plan);

        List<PlanWorkout> planWorkouts = getPlanWorkouts(request, plan);

        planRepository.save(plan);
        planWorkoutRepository.saveAll(planWorkouts);
    }

    @Transactional
    public void update(Long userId, Long planId, PlanSaveOrUpdateRequest request) {
        planValidationService.validatePlanUpdateDateTime(request);

        Plan plan = planRepository.findWithWriterAndWorkoutsById(planId).orElseThrow(
                () -> new IllegalStateException("식별 번호가 " + planId + "에 해당되는 계획이 없습니다.")
        );

        planValidationService.validateOwnerOfPlan(userId, plan);

        List<PlanWorkout> planWorkouts = plan.getPlanWorkouts();
        planWorkoutRepository.deleteAllInBatch(planWorkouts);

        plan.updatePlanInformation(request);

        List<PlanWorkout> newPlanWorkouts = getPlanWorkouts(request, plan);

        planWorkoutRepository.saveAll(newPlanWorkouts);
    }

    private List<PlanWorkout> getPlanWorkouts(
            PlanSaveOrUpdateRequest request,
            Plan plan
    ) {
        Map<Integer, PlanWorkoutRequest> planWorkoutRequestByWorkoutId = getIdsOfWorkouts(request);

        return workoutRepository.findAllByIdIn(planWorkoutRequestByWorkoutId.keySet())
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
