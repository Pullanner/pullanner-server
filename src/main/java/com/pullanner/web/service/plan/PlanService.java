package com.pullanner.web.service.plan;

import com.pullanner.domain.plan.*;
import com.pullanner.domain.user.UserBadge;
import com.pullanner.domain.user.UserBadgeRepository;
import com.pullanner.domain.user.User;
import com.pullanner.domain.user.UserRepository;
import com.pullanner.domain.workout.WorkoutRepository;
import com.pullanner.exception.plan.PlanAccessNoAuthorityException;
import com.pullanner.exception.plan.PlanNotFoundedException;
import com.pullanner.exception.plan.PlanWorkoutNotFoundedException;
import com.pullanner.exception.user.UserNotFoundedException;
import com.pullanner.web.controller.plan.dto.*;
import com.pullanner.web.service.badge.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.pullanner.domain.user.enums.UserExperiencePolicy.*;

@RequiredArgsConstructor
@Service
public class PlanService {

    private final PlanValidationService planValidationService;
    private final BadgeService badgeService;

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final PlanWorkoutRepository planWorkoutRepository;
    private final UserBadgeRepository userBadgeRepository;

    @Transactional(readOnly = true)
    public PlanResponse find(Long userId, Long planId) {
        // validate access authority of plan
        planRepository.findByPlanIdAndUserId(userId, planId).orElseThrow(PlanAccessNoAuthorityException::new);

        Plan plan = planRepository.findWithPlanWorkoutsById(planId).orElseThrow(
                () -> new PlanNotFoundedException(planId)
        );

        return getPlanResponse(plan);
    }

    @Transactional(readOnly = true)
    public PlanResponsesByMonth findByMonth(Long userId, Integer year, Integer month) {
        LocalDateTime firstDateOfThisMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime lastDateOfThisMonth = LocalDateTime.of(year, month, firstDateOfThisMonth.getDayOfMonth(), 23, 59);

        LocalDateTime startDate = firstDateOfThisMonth.minusDays(14);
        LocalDateTime endDate = lastDateOfThisMonth.plusDays(14);

        Map<LocalDate, List<PlanResponse>> planInformationByDate = new TreeMap<>();

        List<Plan> plans = planRepository.findAllWithPlanWorkoutsForPeriodByUserId(startDate, endDate, userId);

        for (Plan plan : plans) {
            LocalDate date = plan.getPlanDateValue();

            if (planInformationByDate.containsKey(date)) {
                List<PlanResponse> planResponses = planInformationByDate.get(date);
                planResponses.add(getPlanResponse(plan));
            } else {
                List<PlanResponse> planResponses = new ArrayList<>();
                planResponses.add(getPlanResponse(plan));
                planInformationByDate.put(date, planResponses);
            }
        }

        return PlanResponsesByMonth.from(planInformationByDate);
    }

    @Transactional
    public void save(Long userId, PlanSaveOrUpdateRequest request) {
        // validate date of plan to save
        planValidationService.validatePlanSaveDate(request);

        User user = userRepository.findWithUserWorkoutsById(userId).orElseThrow(
                () -> new UserNotFoundedException(userId)
        );

        // validate workouts of user's plan by plan type
        planValidationService.validatePlanWorkoutRequestOfUser(user, request);

        Plan plan = Plan.builder()
                .writer(user)
                .name(request.getPlanName())
                .planType(request.getPlanType())
                .planDate(request.getPlanDateTime())
                .build();

        user.addPlan(plan);

        List<PlanWorkout> planWorkouts = getPlanWorkoutsForPlanSaveOrUpdate(request, plan);

        planRepository.save(plan);
        planWorkoutRepository.saveAll(planWorkouts);
    }

    @Transactional
    public void update(Long userId, Long planId, PlanSaveOrUpdateRequest request) {
        // validate access authority of plan
        Plan plan = planRepository.findByPlanIdAndUserId(userId, planId).orElseThrow(PlanAccessNoAuthorityException::new);

        // validate if plan is completed
        planValidationService.validateIfPlanCompleted(plan);

        // validate datetime of plan to update
        planValidationService.validatePlanUpdateDateTime(request);

        plan = planRepository.findWithPlanWorkoutsById(planId).orElseThrow(PlanWorkoutNotFoundedException::new);

        // remove old plan workouts
        List<PlanWorkout> planWorkouts = plan.getPlanWorkouts();
        planWorkoutRepository.deleteAllInBatch(planWorkouts);

        // update plan information
        plan.updatePlanInformation(request);

        // save new plan workouts
        List<PlanWorkout> newPlanWorkouts = getPlanWorkoutsForPlanSaveOrUpdate(request, plan);
        planWorkoutRepository.saveAll(newPlanWorkouts);
    }

    @Transactional
    public void check(Long userId, Long planId, PlanCheckAndNoteRequest request) {
        // validate access authority of plan
        planRepository.findByPlanIdAndUserId(userId, planId).orElseThrow(PlanAccessNoAuthorityException::new);

        Plan plan = planRepository.findWithPlanWorkoutsById(planId).orElseThrow(PlanWorkoutNotFoundedException::new);

        // validate if plan is completed
        planValidationService.validateIfPlanCompleted(plan);

        Map<Integer, PlanWorkout> planWorkoutByStep = plan.getPlanWorkouts()
                .stream()
                .collect(Collectors.toMap(PlanWorkout::getIdOfWorkout, Function.identity(), (key1, key2) -> key1));

        List<PlanWorkoutCheckRequest> planWorkoutChecks = request.getWorkouts();

        for (PlanWorkoutCheckRequest planWorkoutCheck : planWorkoutChecks) {
            int step = planWorkoutCheck.getStep();
            if (planWorkoutByStep.containsKey(step)) {
                PlanWorkout planWorkout = planWorkoutByStep.get(step);
                planWorkout.updateStatus(planWorkoutCheck.getDone());
            }
        }

        // check completion of plan
        if (plan.checkCompletion()) {
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new UserNotFoundedException(userId)
            );

            List<Plan> completedPlansOfUser = planRepository.findCompletedPlansByUserId(userId);

            // 1. check if completed plan is first
            if (completedPlansOfUser.isEmpty()) {
                user.updateExperiencePoint(EXPERIENCE_BADGE_FIRST_PLAN);
                badgeService.saveFirstPlanBadge(user);
            } else {
                // 2. check if completed plan is hundredth
                if (completedPlansOfUser.size() == 99) {
                    user.updateExperiencePoint(EXPERIENCE_BADGE_ONE_HUNDRED_PLAN);
                    badgeService.saveOneHundredPlanBadge(user);
                // 3. check if completed plan is multiples of 1000
                } else if ((completedPlansOfUser.size() + 1) % 1000 == 0) {
                    user.updateExperiencePoint(EXPERIENCE_BADGE_PULL_UP_KING);
                    badgeService.savePullUpKingBadge(user);
                }

                // 4. check if all workouts (set) is completed
                // 4-1. check if user chooses to be able to perform all of the actions
                if (user.isAllWorkoutsPossible()) {
                    Optional<UserBadge> allRoundBadge = userBadgeRepository.findAllRoundBadgeByUserId(userId);
                    // 4-2. check if user has not acquired all rounder badge
                    if (allRoundBadge.isEmpty()) {
                        List<Long> idsOfCompletedPlansOfUser = completedPlansOfUser.stream()
                                .filter(Plan::isStrengthPlanType)
                                .map(Plan::getId)
                                .toList();

                        List<PlanWorkout> planWorkouts = planWorkoutRepository.findByPlanIds(idsOfCompletedPlansOfUser);

                        Set<Integer> idsOfCompletedWorkouts = planWorkouts.stream()
                                .map(PlanWorkout::getIdOfWorkout)
                                .collect(Collectors.toSet());

                        // 4-3. check if user has completed the plan for all pull-up workouts
                        if (idsOfCompletedWorkouts.size() == 8) {
                            user.updateExperiencePoint(EXPERIENCE_BADGE_ALL_ROUNDER);
                            badgeService.saveAllRounderBadge(user);
                        }
                    }
                }

                // 5. check if plan of master type is multiples of 30
                if (plan.isMasterPlanType()) {
                    long countOfCompletedPlansOfMasterType = completedPlansOfUser.stream()
                            .filter(Plan::isMasterPlanType)
                            .count();

                    if ((countOfCompletedPlansOfMasterType + 1) % 30 == 0) {
                        user.updateExperiencePoint(EXPERIENCE_BADGE_EXPERIENCE_KING);
                        badgeService.saveExperienceKingBadge(user);
                    }
                // 6. check if plan of strength type is multiples of 30
                } else if (plan.isStrengthPlanType()) {
                    long countOfCompletedPlansOfStrengthType = completedPlansOfUser.stream()
                            .filter(Plan::isStrengthPlanType)
                            .count();

                    if ((countOfCompletedPlansOfStrengthType + 1) % 30 == 0) {
                        user.updateExperiencePoint(EXPERIENCE_BADGE_MUSCLE_KING);
                        badgeService.saveMuscleKingBadge(user);
                    }
                }

                // 7. check if user has achieved the plan for seven consecutive days
                LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
                for (Plan completedPlan : completedPlansOfUser) {
                    if (completedPlan.checkCompletionDateForSameDate(today)) {
                        continue;
                    }

                    if (completedPlan.checkCompletionDateForOneDayBefore(today)) {
                        if (user.getSequenceCompletionDays() == 6) {
                            user.updateExperiencePoint(EXPERIENCE_BADGE_SEVEN_SEQUENCE_PLAN_COMPLETED);
                            user.initSequenceCompletionDays();
                            badgeService.saveSevenSequencePlanCompletionBadge(user);
                        } else {
                            user.plusSequenceCompletionDays();
                        }
                    } else {
                        user.initSequenceCompletionDays();
                    }

                    break;
                }
            }

            plan.completePlan();

            user.updateExperiencePoint(EXPERIENCE_PLAN_COMPLETED);
        }

        plan.updateNote(request.getNote());
    }

    @Transactional
    public void delete(Long userId, Long planId) {
        // validate access authority of plan
        Plan plan = planRepository.findByPlanIdAndUserId(userId, planId).orElseThrow(PlanAccessNoAuthorityException::new);

        // validate if plan is completed
        planValidationService.validateIfPlanCompleted(plan);

        planRepository.delete(plan);
    }

    private PlanResponse getPlanResponse(Plan plan) {
        List<PlanWorkoutResponse> planWorkoutResponses = plan.getPlanWorkoutResponses();
        int progress = plan.getProgress();
        int mainWorkoutStep = plan.getMainWorkoutStep();

        return PlanResponse.of(plan, planWorkoutResponses, progress, mainWorkoutStep);
    }

    private List<PlanWorkout> getPlanWorkoutsForPlanSaveOrUpdate(
            PlanSaveOrUpdateRequest request,
            Plan plan
    ) {
        Map<Integer, PlanWorkoutRequest> planWorkoutRequestByWorkoutId = getPlanWorkoutRequestByWorkoutId(request);

        return workoutRepository.findAllByIdIn(planWorkoutRequestByWorkoutId.keySet())
                .stream()
                .map(workout -> {
                    PlanWorkoutRequest planWorkoutRequest = planWorkoutRequestByWorkoutId.get(workout.getId());
                    PlanWorkout planWorkout = PlanWorkout.builder()
                            .plan(plan)
                            .workout(workout)
                            .set(planWorkoutRequest.getSet())
                            .countPerSet(planWorkoutRequest.getCount())
                            .done(planWorkoutRequest.getDone())
                            .build();

                    plan.addPlanWorkout(planWorkout);
                    workout.addPlanWorkout(planWorkout);

                    return planWorkout;
                })
                .toList();
    }

    private Map<Integer, PlanWorkoutRequest> getPlanWorkoutRequestByWorkoutId(PlanSaveOrUpdateRequest request) {
        return request.getWorkouts()
                .stream()
                .collect(Collectors.toMap(
                        PlanWorkoutRequest::getStep,
                        Function.identity()
                ));
    }
}
