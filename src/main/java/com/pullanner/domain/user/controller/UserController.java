package com.pullanner.domain.user.controller;

import com.pullanner.domain.user.dto.UserResponse;
import com.pullanner.domain.user.dto.UserUpdateRequest;
import com.pullanner.domain.user.exception.InvalidMailAuthorizationCodeException;
import com.pullanner.domain.user.service.UserService;
import com.pullanner.global.api.ApiResponseCode;
import com.pullanner.global.api.ApiResponseMessage;
import com.pullanner.global.auth.jwt.argumentresolver.RefreshTokenId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.pullanner.global.api.ApiUtil.getResponseEntity;

@RequiredArgsConstructor
@RestController
public class UserController {

    private static final int NICKNAME_MIN_LENGTH = 2;
    private static final int NICKNAME_MAX_LENGTH = 8;

    private final UserService userService;

    @GetMapping("/api/users")
    public UserResponse find(@AuthenticationPrincipal Long userId) {
        return userService.findById(userId);
    }

    @GetMapping("/api/users/duplicate")
    public ResponseEntity<ApiResponseMessage> validate(@RequestParam @Length(min = NICKNAME_MIN_LENGTH, max = NICKNAME_MAX_LENGTH) String nickName) {
        return userService.validateDuplicate(nickName);
    }

    @PostMapping("/api/users")
    public UserResponse register(@AuthenticationPrincipal Long userId, @Valid @RequestBody UserUpdateRequest userInfo) {
        return userService.register(userId, userInfo);
    }

    @PostMapping("/api/users/mail")
    public ResponseEntity<ApiResponseMessage> mail(@AuthenticationPrincipal Long userId) {
        userService.sendMail(userId);
        return getResponseEntity(ApiResponseCode.USER_EMAIL_SENDING_SUCCESS);
    }

    @DeleteMapping("/api/users")
    public ResponseEntity<ApiResponseMessage> delete(@AuthenticationPrincipal Long userId,
        @RefreshTokenId String refreshTokenId, @RequestParam Integer code) {
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
