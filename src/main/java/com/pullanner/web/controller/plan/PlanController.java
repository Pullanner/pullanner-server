package com.pullanner.web.controller.plan;

import com.pullanner.exception.plan.*;
import com.pullanner.web.ApiResponseCode;
import com.pullanner.web.ApiResponseMessage;
import com.pullanner.web.controller.plan.dto.PlanResponse;
import com.pullanner.web.controller.plan.dto.PlanSaveOrUpdateRequest;
import com.pullanner.web.service.plan.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.pullanner.web.ApiUtil.getResponseEntity;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "Plan", description = "Plan API")
@ApiResponses(
        {
                @ApiResponse(responseCode = "401", description = "INVALID AUTHENTICATION REQUEST", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class))),
                @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class))),
                @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
        }
)
@RestController
public class PlanController {

    private final PlanService planService;
    @Operation(summary = "철봉 운동 계획 단건 조회", description = "사용자가 등록한 철봉 운동 계획을 조회할 수 있는 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PlanResponse.class)))
    @GetMapping("/api/plans/{id}")
    public PlanResponse find(
            @PathVariable("id") @Parameter(name = "Plan id", description = "조회할 철봉 운동 계획의 고유 아이디 값", example = "1") Long planId
    ) {
        return planService.find(planId);
    }

    @Operation(summary = "철봉 운동 계획 생성", description = "사용자가 철봉 운동 계획을 등록할 수 있는 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
    @PostMapping("/api/plans")
    public ResponseEntity<ApiResponseMessage> register(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PlanSaveOrUpdateRequest request
            ) {
        planService.save(userId, request);
        return getResponseEntity(ApiResponseCode.PLAN_SAVED);
    }

    @Operation(summary = "철봉 운동 계획 수정", description = "사용자가 철봉 운동 계획을 수정할 수 있는 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
    @PatchMapping("/api/plans/{id}")
    public ResponseEntity<ApiResponseMessage> update(
            @AuthenticationPrincipal Long userId,
            @PathVariable("id") @Parameter(name = "Plan id", description = "수정할 철봉 운동 계획의 고유 아이디 값", example = "1") Long planId,
            @Valid @RequestBody PlanSaveOrUpdateRequest request
    ) {
        planService.update(userId, planId, request);
        return getResponseEntity(ApiResponseCode.PLAN_UPDATED);
    }

    @ExceptionHandler(PlanSaveDateException.class)
    public ResponseEntity<ApiResponseMessage> handlePlanSaveDateTimeException(PlanSaveDateException e) {
        log.error("", e);
        return getResponseEntity(ApiResponseCode.PLAN_SAVE_DATE_INVALID);
    }

    @ExceptionHandler(PlanUpdateDateTimeException.class)
    public ResponseEntity<ApiResponseMessage> handlePlanUpdateDateTimeException(PlanUpdateDateTimeException e) {
        log.error("", e);
        return getResponseEntity(ApiResponseCode.PLAN_UPDATE_DATETIME_INVALID);
    }

    @ExceptionHandler(PlanUpdateNoAuthorityException.class)
    public ResponseEntity<ApiResponseMessage> handlePlanUpdateNoAuthorityException(PlanUpdateNoAuthorityException e) {
        log.error("", e);
        return getResponseEntity(ApiResponseCode.PLAN_UPDATE_NO_AUTHORITY);
    }

    @ExceptionHandler(PlanNotFoundedException.class)
    public ResponseEntity<ApiResponseMessage> handlePlanNotFoundedException(PlanNotFoundedException e) {
        log.error("", e);
        return getResponseEntity(ApiResponseCode.PLAN_NOT_FOUNDED);
    }

    @ExceptionHandler(PlanWorkoutNotFoundedException.class)
    public ResponseEntity<ApiResponseMessage> handlePlanWorkoutNotFoundedException(PlanWorkoutNotFoundedException e) {
        log.error("", e);
        return getResponseEntity(ApiResponseCode.PLAN_WORKOUT_NOT_FOUNDED);
    }
}
