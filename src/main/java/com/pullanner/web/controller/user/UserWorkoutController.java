package com.pullanner.web.controller.user;

import com.pullanner.web.ApiResponseCode;
import com.pullanner.web.ApiResponseMessage;
import com.pullanner.web.controller.user.dto.UserWorkoutResponse;
import com.pullanner.web.controller.user.dto.UserWorkoutSaveRequest;
import com.pullanner.web.service.user.UserWorkoutService;
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

    @GetMapping("/api/users/workouts")
    public UserWorkoutResponse find(@AuthenticationPrincipal Long userId) {
        return null;
    }

    @PostMapping("/api/users/workouts")
    public ResponseEntity<ApiResponseMessage> register(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody UserWorkoutSaveRequest userWorkoutInfo
            ) {
        userWorkoutService.save(userId, userWorkoutInfo);
        return getResponseEntity(ApiResponseCode.USER_WORKOUT_CREATED);
    }

    @PatchMapping("/api/users/workouts")
    public ResponseEntity<ApiResponseMessage> update(@AuthenticationPrincipal Long userId) {
        return null;
    }
}
