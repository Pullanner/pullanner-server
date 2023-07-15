package com.pullanner.domain.user.controller;

import com.pullanner.domain.user.dto.UserResponse;
import com.pullanner.domain.user.dto.UserUpdateRequest;
import com.pullanner.domain.user.exception.InvalidMailAuthorizationCodeException;
import com.pullanner.domain.user.service.UserService;
import com.pullanner.global.api.ApiResponseCode;
import com.pullanner.global.api.ApiResponseMessage;
import com.pullanner.global.auth.jwt.argumentresolver.RefreshTokenId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.pullanner.global.api.ApiUtil.getResponseEntity;

@Tag(name = "User", description = "User API")
@ApiResponses(
    {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class))),
        @ApiResponse(responseCode = "401", description = "Invalid Auth REQUEST", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class))),
        @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class))),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
    }
)
@RequiredArgsConstructor
@RestController
public class UserController {

    private static final int NICKNAME_MIN_LENGTH = 2;
    private static final int NICKNAME_MAX_LENGTH = 8;

    private final UserService userService;

    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회하는 기능입니다.")
    @GetMapping("/api/users")
    public UserResponse find(@AuthenticationPrincipal Long userId) {
        return userService.findById(userId);
    }

    @Operation(summary = "사용자 닉네임 중복 검사", description = "사용자 닉네임의 중복 여부를 검사하는 기능입니다.")
    @GetMapping("/api/users/duplicate")
    public ResponseEntity<ApiResponseMessage> validate(
        @RequestParam @Length(min = NICKNAME_MIN_LENGTH, max = NICKNAME_MAX_LENGTH) @Parameter(name = "nickname", description = "사용자 닉네임", example = "ikjo") String nickname) {
        return userService.validateDuplicate(nickname);
    }

    @Operation(summary = "사용자 닉네임 등록(수정)", description = "사용자 닉네임을 등록하거나 수정할 수 있는 기능입니다.")
    @PostMapping("/api/users")
    public UserResponse register(
        @AuthenticationPrincipal Long userId,
        @Valid @RequestBody @Parameter(name = "nickname", description = "사용자 닉네임") UserUpdateRequest userInfo) {
        return userService.register(userId, userInfo);
    }

    @Operation(summary = "사용자 이메일 인증 코드 발송", description = "사용자의 회원 탈퇴 요청을 처리하기 위해 사용자 이메일로 인증 코드를 발송하는 기능입니다.")
    @PostMapping("/api/users/mail")
    public ResponseEntity<ApiResponseMessage> mail(@AuthenticationPrincipal Long userId) {
        userService.sendMail(userId);
        return getResponseEntity(ApiResponseCode.USER_EMAIL_SENDING_SUCCESS);
    }

    @Operation(summary = "사용자 이메일 인증 코드 발송", description = "사용자의 회원 탈퇴 요청을 처리하기 위해 사용자 이메일로 인증 코드를 발송하는 기능입니다.")
    @DeleteMapping("/api/users")
    public ResponseEntity<ApiResponseMessage> delete(
        @AuthenticationPrincipal Long userId,
        @RefreshTokenId @Parameter(hidden = true)  String refreshTokenId,
        @RequestParam @Parameter(name = "인증 코드", description = "회원 탈퇴 처리를 위한 인증 코드", example = "123456") Integer code) {
        userService.deleteUser(userId, refreshTokenId, code);
        return getResponseEntity(ApiResponseCode.USER_DELETED_SUCCESS);
    }

    @ExceptionHandler(InvalidMailAuthorizationCodeException.class)
    public ResponseEntity<ApiResponseMessage> handleInvalidMailAuthorizationCodeException() {
        return getResponseEntity(ApiResponseCode.USER_INVALID_MAIL_AUTHORIZATION_CODE);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponseMessage> handleUserNotFoundException() {
        return getResponseEntity(ApiResponseCode.USER_NOT_FOUND);
    }
}
