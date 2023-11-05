package com.pullanner.web.controller.summary;

import com.pullanner.web.ApiResponseMessage;
import com.pullanner.web.controller.summary.dto.CompletedPlanCountByTime;
import com.pullanner.web.controller.summary.dto.TotalMonthlyWorkoutCountResponse;
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

import java.time.LocalDateTime;
import java.time.ZoneId;

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

    @Operation(summary = "풀업 운동별 월별 동작 횟수 조회", description = "사용자의 풀업 운동별 월별(최근 6개월) 동작 횟수를 조회할 수 있는 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = TotalMonthlyWorkoutCountResponse.class)))
    @GetMapping("/api/summary/month-workout-count")
    public TotalMonthlyWorkoutCountResponse getTotalCountByMonthAndWorkout(@AuthenticationPrincipal Long userId) {
        LocalDateTime endDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime startDate = getPreviousMonthOfLocalDate(endDate, 5);
        return summaryService.getTotalCountByWorkoutNameForPeriod(userId, startDate, endDate);
    }

    private LocalDateTime getPreviousMonthOfLocalDate(LocalDateTime localDate, int previousMonth) {
        return localDate.minusMonths(previousMonth)
                .minusDays(localDate.getDayOfMonth() - 1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0);
    }

    @Operation(summary = "운동 시간대별 완료된 철봉 운동 계획 개수 조회", description = "사용자의 운동 시간대별 완료된 철봉 운동 계획 개수를 조회할 수 있는 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = TotalMonthlyWorkoutCountResponse.class)))
    @GetMapping("/api/summary/completed-plan-count")
    public CompletedPlanCountByTime getCompletedPlanCountByTime(@AuthenticationPrincipal Long userId) {
        LocalDateTime endDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime startDate = getPreviousMonthOfLocalDate(endDate, 1);
        return summaryService.getCompletedPlanCountByTime(userId, startDate, endDate);
    }
}
