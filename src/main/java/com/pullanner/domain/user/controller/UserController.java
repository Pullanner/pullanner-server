package com.pullanner.domain.user.controller;

import com.pullanner.domain.user.dto.UserResponseDto;
import com.pullanner.domain.user.dto.UserUpdateRequestDto;
import com.pullanner.domain.user.service.UserService;
import com.pullanner.global.ApiResponseCode;
import com.pullanner.global.ApiResponseMessage;
import com.pullanner.global.auth.jwt.dto.JwtAuthenticationResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/api/users")
    public UserResponseDto find() {
        JwtAuthenticationResult jwtAuthenticationResult = (JwtAuthenticationResult) SecurityContextHolder.getContext().getAuthentication();
        String email = jwtAuthenticationResult.getEmail();
        return userService.findByEmail(email);
    }

    @PatchMapping("/api/users/{id}")
    public ApiResponseMessage update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequestDto request) {
        userService.update(id, request);
        return new ApiResponseMessage(ApiResponseCode.USER_UPDATE_SUCCESS.getCode(),
            ApiResponseCode.USER_UPDATE_SUCCESS.getMessage());
    }
}
