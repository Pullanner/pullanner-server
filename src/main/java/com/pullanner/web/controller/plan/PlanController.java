package com.pullanner.web.controller.plan;

import com.pullanner.exception.plan.PlanSaveDateException;
import com.pullanner.exception.plan.PlanUpdateDateTimeException;
import com.pullanner.exception.plan.PlanUpdateNoAuthorityException;
import com.pullanner.web.ApiResponseCode;
import com.pullanner.web.ApiResponseMessage;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.pullanner.web.ApiUtil.getResponseEntity;

@Tag(name = "Plan", description = "Plan API")
@ApiResponses(
        {
                @ApiResponse(responseCode = "401", description = "INVALID AUTHENTICATION REQUEST", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class))),
                @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class))),
                @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
        }
)
@RequiredArgsConstructor
@RestController
public class PlanController {

    private final PlanService planService;

    @Operation(summary = "철봉 운동 계획 생성", description = "사용자가 철봉 운동 계획을 등록할 수 있는 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
    @PostMapping("/api/plans")
    public ResponseEntity<ApiResponseMessage> register(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody PlanSaveOrUpdateRequest request
            ) {
        planService.save(1L, request);
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
        planService.update(1L, planId, request);
        return getResponseEntity(ApiResponseCode.PLAN_UPDATED);
    }

    @ExceptionHandler(PlanSaveDateException.class)
    public ResponseEntity<ApiResponseMessage> handlePlanSaveDateTimeException(PlanSaveDateException e) {
        e.printStackTrace();
        return getResponseEntity(ApiResponseCode.PLAN_SAVE_DATE_INVALID);
    }

    @ExceptionHandler(PlanUpdateDateTimeException.class)
    public ResponseEntity<ApiResponseMessage> handlePlanUpdateDateTimeException(PlanUpdateDateTimeException e) {
        e.printStackTrace();
        return getResponseEntity(ApiResponseCode.PLAN_UPDATE_DATETIME_INVALID);
    }

    @ExceptionHandler(PlanUpdateNoAuthorityException.class)
    public ResponseEntity<ApiResponseMessage> handlePlanUpdateNoAuthorityException(PlanUpdateNoAuthorityException e) {
        e.printStackTrace();
        return getResponseEntity(ApiResponseCode.PLAN_UPDATE_NO_AUTHORITY);
    }
}
