package com.pullanner.web.service.plan;

import com.pullanner.domain.plan.Plan;
import com.pullanner.domain.plan.PlanRepository;
import com.pullanner.domain.plan.PlanWorkout;
import com.pullanner.domain.plan.PlanWorkoutRepository;
import com.pullanner.domain.user.User;
import com.pullanner.domain.user.UserRepository;
import com.pullanner.domain.workout.WorkoutRepository;
import com.pullanner.exception.plan.PlanAccessNoAuthorityException;
import com.pullanner.exception.plan.PlanNotFoundedException;
import com.pullanner.exception.plan.PlanWorkoutNotFoundedException;
import com.pullanner.exception.user.UserNotFoundedException;
import com.pullanner.web.controller.plan.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PlanService {

    private final PlanValidationService planValidationService;

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final PlanWorkoutRepository planWorkoutRepository;

    @Transactional(readOnly = true)
    public PlanResponse find(Long userId, Long planId) {
        // validate access authority of plan
        planRepository.findByPlanIdAndUserId(userId, planId).orElseThrow(PlanAccessNoAuthorityException::new);

        Plan plan = planRepository.findWithPlanWorkoutsAndWorkoutsById(planId).orElseThrow(
                () -> new PlanNotFoundedException("식별 번호가 " + planId + "에 해당되는 계획이 없습니다.")
        );

        List<PlanWorkoutResponse> planWorkoutResponses = plan.getPlanWorkoutResponses();
        int progress = plan.getProgress();
        int mainWorkoutStep = plan.getMainWorkoutStep();

        return PlanResponse.of(plan, planWorkoutResponses, progress, mainWorkoutStep);
    }

    @Transactional
    public void save(Long userId, PlanSaveOrUpdateRequest request) {
        // validate date of plan to save
        planValidationService.validatePlanSaveDate(request);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundedException("식별 번호가 " + userId + "에 해당되는 사용자가 없습니다.")
        );

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
        // validate access authority of plan
        planRepository.findByPlanIdAndUserId(userId, planId).orElseThrow(PlanAccessNoAuthorityException::new);

        // validate datetime of plan to update
        planValidationService.validatePlanUpdateDateTime(request);

        Plan plan = planRepository.findWithPlanWorkoutsById(planId).orElseThrow(
                () -> new PlanNotFoundedException("식별 번호가 " + planId + "에 해당되는 계획이 없습니다.")
        );

        List<PlanWorkout> planWorkouts = plan.getPlanWorkouts();
        planWorkoutRepository.deleteAllInBatch(planWorkouts);

        plan.updatePlanInformation(request);

        List<PlanWorkout> newPlanWorkouts = getPlanWorkouts(request, plan);

        planWorkoutRepository.saveAll(newPlanWorkouts);
    }

    @Transactional
    public void check(Long userId, Long planId, PlanCheckAndNoteRequest request) {
        // validate access authority of plan
        Plan plan = planRepository.findByPlanIdAndUserId(userId, planId).orElseThrow(PlanAccessNoAuthorityException::new);

        List<PlanWorkoutCheckRequest> planWorkoutChecks = request.getWorkouts();

        Map<Integer, PlanWorkout> planWorkoutByStep = planRepository.findWithPlanWorkoutsAndWorkoutsById(planId)
                .orElseThrow(PlanWorkoutNotFoundedException::new)
                .getPlanWorkouts()
                .stream()
                .collect(Collectors.toMap(PlanWorkout::getStepOfWorkout, Function.identity(), (key1, key2) -> key1));

        for (PlanWorkoutCheckRequest planWorkoutCheck : planWorkoutChecks) {
            int step = planWorkoutCheck.getStep();
            if (planWorkoutByStep.containsKey(step)) {
                PlanWorkout planWorkout = planWorkoutByStep.get(step);
                planWorkout.updateStatus(planWorkoutCheck.getDone());
            }
        }

        plan.updateNote(request.getNote());
    }

    @Transactional
    public void delete(Long userId, Long planId) {
        // validate access authority of plan
        Plan plan = planRepository.findByPlanIdAndUserId(userId, planId).orElseThrow(PlanAccessNoAuthorityException::new);

        planRepository.delete(plan);
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
