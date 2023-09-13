package com.pullanner.web.service.plan;

import com.pullanner.domain.plan.Plan;
import com.pullanner.domain.plan.PlanType;
import com.pullanner.domain.user.User;
import com.pullanner.exception.plan.*;
import com.pullanner.web.controller.plan.dto.PlanSaveOrUpdateRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;


@Service
public class PlanValidationService {

    public void validatePlanSaveDate(PlanSaveOrUpdateRequest request) {
        LocalDate today = LocalDate.now();
        LocalDate planDate = request.getPlanDateTime().toLocalDate();

        if (!planDate.isAfter(today)) {
            throw new PlanSaveDateException();
        }
    }

    public void validatePlanUpdateDateTime(PlanSaveOrUpdateRequest request) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime planDateTime = request.getPlanDateTime().truncatedTo(ChronoUnit.MINUTES);

        // 수정할 계획 일시(yyyy-MM-dd HH:mm 기준)가 현재 시간 보다 이전인 경우 예외 발생
        if (planDateTime.isBefore(now)) {
            throw new PlanUpdateDateTimeException();
        }
    }

    public void validateIfPlanCompleted(Plan plan) {
        if (plan.isCompleted()) {
            throw new PlanCompletedNotChangedException();
        }
    }

    public void validatePlanWorkoutRequestOfUser(User user, PlanSaveOrUpdateRequest request) {
        Set<Integer> idSetOfUserPossibleWorkouts = user.getIdSetOfPossibleWorkout();
        Set<Integer> idSetOfImpossibleWorkouts = user.getIdSetOfImpossibleWorkout();

        Set<Integer> workoutIdSetOfPlanSaveRequest = request.getWorkoutIdSetOfPlanSaveRequest();

        PlanType planType = request.getPlanType();

        if (PlanType.MASTER.equals(planType)) {
            if (!idSetOfImpossibleWorkouts.containsAll(workoutIdSetOfPlanSaveRequest)) {
                throw new InvalidPlanSaveRequestException();
            }
        } else {
            if (!idSetOfUserPossibleWorkouts.containsAll(workoutIdSetOfPlanSaveRequest)) {
                throw new InvalidPlanSaveRequestException();
            }
        }
    }
}
