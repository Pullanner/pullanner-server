package com.pullanner.web.controller.plan;

import com.pullanner.web.ApiResponseCode;
import com.pullanner.web.ApiResponseMessage;
import com.pullanner.web.controller.plan.dto.PlanSaveOrUpdateRequest;
import com.pullanner.web.service.plan.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.pullanner.web.ApiUtil.getResponseEntity;

@RequiredArgsConstructor
@RestController
public class PlanController {

    private final PlanService planService;

    @PostMapping("/api/plans")
    public ResponseEntity<ApiResponseMessage> register(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PlanSaveOrUpdateRequest request
            ) {
        planService.save(userId, request);
        return getResponseEntity(ApiResponseCode.PLAN_CREATED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        return getResponseEntity(ApiResponseCode.PLAN_DATETIME_INVALID);
    }
}
