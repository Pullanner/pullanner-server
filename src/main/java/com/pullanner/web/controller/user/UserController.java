package com.pullanner.web.controller.user;

import com.pullanner.exception.user.AlreadySentAuthorizationCodeException;
import com.pullanner.exception.user.ProfileImageUploadException;
import com.pullanner.exception.user.UserNotFoundedException;
import com.pullanner.web.controller.user.dto.UserResponse;
import com.pullanner.web.controller.user.dto.UserNicknameUpdateRequest;
import com.pullanner.exception.user.InvalidMailAuthorizationCodeException;
import com.pullanner.web.service.user.UserService;
import com.pullanner.web.ApiResponseCode;
import com.pullanner.web.ApiResponseMessage;
import com.pullanner.web.argumentresolver.RefreshTokenId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.pullanner.web.ApiUtil.getResponseEntity;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
@ApiResponses(
    {
        @ApiResponse(responseCode = "401", description = "INVALID AUTHENTICATION REQUEST", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class))),
        @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class))),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
    }
)
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회하는 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @GetMapping("/api/users")
    public UserResponse find(@AuthenticationPrincipal Long userId) {
        return userService.findById(userId);
    }

    @Operation(summary = "사용자 닉네임 중복 검사", description = "사용자 닉네임의 중복 여부를 검사하는 기능입니다.")
    @ApiResponse(responseCode = "400", description = "유효하지 않은 닉네임(글자수 제약 위반)", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
    @GetMapping("/api/users/duplicate")
    public ResponseEntity<ApiResponseMessage> validate(
        @RequestParam @Parameter(name = "nickname", description = "사용자 닉네임", example = "ikjo") String nickname) {
        return userService.validateDuplicate(nickname);
    }

    @Operation(summary = "사용자 닉네임 등록(수정)", description = "사용자 닉네임을 등록하거나 수정할 수 있는 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @PostMapping("/api/users")
    public UserResponse register(
        @AuthenticationPrincipal Long userId,
        @Valid @RequestBody @Parameter(name = "nickname", description = "사용자 닉네임") UserNicknameUpdateRequest userInfo) {
        return userService.register(userId, userInfo);
    }

    @Operation(summary = "사용자 프로필 사진 수정", description = "사용자의 프로필 사진을 수정할 수 있는 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @PatchMapping("/api/users")
    public UserResponse update(
        @AuthenticationPrincipal Long userId,
        @RequestParam @NotNull @Parameter(name = "profileImage", description = "사용자 프로필 사진 파일", in = ParameterIn.QUERY) MultipartFile profileImage) {
        return userService.updateProfileImage(userId, profileImage);
    }

    @Operation(summary = "사용자 이메일 인증 코드 발송", description = "사용자의 회원 탈퇴 요청을 처리하기 위해 사용자 이메일로 인증 코드를 발송하는 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
    @PostMapping("/api/users/mail")
    public ResponseEntity<ApiResponseMessage> mail(@AuthenticationPrincipal Long userId) {
        userService.sendMail(userId);
        return getResponseEntity(ApiResponseCode.USER_EMAIL_SENDING_SUCCESS);
    }

    @Operation(summary = "사용자 이메일 인증 코드 발송", description = "사용자의 회원 탈퇴 요청을 처리하기 위해 사용자 이메일로 인증 코드를 발송하는 기능입니다.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ApiResponseMessage.class)))
    @DeleteMapping("/api/users")
    public ResponseEntity<ApiResponseMessage> delete(
        @AuthenticationPrincipal Long userId,
        @RefreshTokenId @Parameter(hidden = true) String refreshTokenId,
        @RequestParam @Parameter(name = "인증 코드", description = "회원 탈퇴 처리를 위한 인증 코드", example = "123456") Integer code) {
        userService.deleteUser(userId, refreshTokenId, code);
        return getResponseEntity(ApiResponseCode.USER_DELETED_SUCCESS);
    }

    @ExceptionHandler(InvalidMailAuthorizationCodeException.class)
    public ResponseEntity<ApiResponseMessage> handleInvalidMailAuthorizationCodeException(InvalidMailAuthorizationCodeException e) {
        log.error("", e);
        return getResponseEntity(ApiResponseCode.USER_INVALID_MAIL_AUTHORIZATION_CODE);
    }

    @ExceptionHandler(UserNotFoundedException.class)
    public ResponseEntity<ApiResponseMessage> handleUserNotFoundedException(UserNotFoundedException e) {
        log.error("", e);
        return getResponseEntity(ApiResponseCode.USER_NOT_FOUND);
    }

    @ExceptionHandler(ProfileImageUploadException.class)
    public ResponseEntity<ApiResponseMessage> handleProfileImageUploadException(ProfileImageUploadException e) {
        log.error("", e);
        return getResponseEntity(ApiResponseCode.USER_PROFILE_IMAGE_UPLOAD_FAIL);
    }

    @ExceptionHandler(AlreadySentAuthorizationCodeException.class)
    public ResponseEntity<ApiResponseMessage> handleAlreadySentAuthorizationCodeException(AlreadySentAuthorizationCodeException e) {
        log.error("", e);
        return getResponseEntity(ApiResponseCode.USER_ALREADY_SENT_AUTHORIZATION_CODE);
    }
}
