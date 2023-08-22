package com.pullanner.web.controller.user;

import com.pullanner.web.ApiResponseCode;
import com.pullanner.web.ApiResponseMessage;
import com.pullanner.web.controller.user.dto.UserWorkoutResponse;
import com.pullanner.web.controller.user.dto.UserWorkoutSaveOrUpdateRequest;
import com.pullanner.web.service.user.UserWorkoutService;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "User Workout", description = "User Workout API")
@ApiResponses(
        {
                @ApiResponse(responseCode = "401", description = "INVALID AUTHENTICATION REQUEST", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class))),
                @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class))),
                @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
        }
)
@RequiredArgsConstructor
@RestController
public class UserWorkoutController {

    private final UserWorkoutService userWorkoutService;

    @Operation(summary = "사용자 가능 철봉 동작 조회", description = "사용자가 수행할 수 있는 철봉 동작들을 조회하는 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserWorkoutResponse.class)))
    @GetMapping("/api/users/workouts")
    public UserWorkoutResponse find(@AuthenticationPrincipal Long userId) {
        return userWorkoutService.findByUserId(userId);
    }

    @Operation(summary = "사용자 가능 철봉 동작 등록", description = "사용자가 수행할 수 있는 철봉 동작들을 등록하는 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
    @PostMapping("/api/users/workouts")
    public ResponseEntity<ApiResponseMessage> register(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody UserWorkoutSaveOrUpdateRequest userWorkoutInfo
            ) {
        userWorkoutService.save(userId, userWorkoutInfo);
        return getResponseEntity(ApiResponseCode.USER_WORKOUT_CREATED);
    }

    @Operation(summary = "사용자 가능 철봉 동작 수정", description = "사용자가 수행할 수 있는 철봉 동작들을 수정하는 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
    @PatchMapping("/api/users/workouts")
    public ResponseEntity<ApiResponseMessage> update(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody UserWorkoutSaveOrUpdateRequest userWorkoutInfo
    ) {
        userWorkoutService.update(userId, userWorkoutInfo);
        return getResponseEntity(ApiResponseCode.USER_WORKOUT_UPDATED);
    }
}
