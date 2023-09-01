package com.pullanner.web.service.plan;

import com.pullanner.domain.plan.Plan;
import com.pullanner.domain.plan.PlanRepository;
import com.pullanner.domain.user.User;
import com.pullanner.exception.plan.PlanNotFoundedException;
import com.pullanner.exception.plan.PlanSaveDateException;
import com.pullanner.exception.plan.PlanUpdateDateTimeException;
import com.pullanner.exception.plan.PlanUpdateNoAuthorityException;
import com.pullanner.web.controller.plan.dto.PlanSaveOrUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

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

    public void validateOwnerOfPlan(Long userId, Plan plan) {
        User writer = plan.getWriter();

        if (!Objects.equals(writer.getId(), userId)) {
            throw new PlanUpdateNoAuthorityException();
        }
    }
}
