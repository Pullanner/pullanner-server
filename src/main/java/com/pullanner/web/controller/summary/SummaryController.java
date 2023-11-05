package com.pullanner.web.controller.summary;

import com.pullanner.web.ApiResponseMessage;
import com.pullanner.web.controller.summary.dto.TotalWorkoutCountResponse;
import com.pullanner.web.service.summary.SummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "Summary", description = "Summary API")
@ApiResponses(
        {
                @ApiResponse(responseCode = "401", description = "INVALID AUTHENTICATION REQUEST", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class))),
                @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class))),
                @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
        }
)
@RestController
public class SummaryController {

    private final SummaryService summaryService;

    @Operation(summary = "풀업 운동별 총 동작 횟수 조회", description = "사용자의 풀업 운동별 총 동작 횟수를 조회할 수 있는 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = TotalWorkoutCountResponse.class)))
    @GetMapping("/api/summary/total-workout-count")
    public TotalWorkoutCountResponse getTotalCountByWorkout(@AuthenticationPrincipal Long userId) {
        return summaryService.getTotalCountByWorkout(userId);
    }

    @GetMapping("/api/summary/month-workout-count")
    public void getTotalCountByMonthAndWorkout() {

    }

    @GetMapping("/api/summary/completed-plan-count")
    public void getCompletedPlanCountByTime() {

    }
}
