package com.pullanner.domain.user.controller;

import com.pullanner.domain.user.dto.UserResponseDto;
import com.pullanner.domain.user.dto.UserUpdateRequestDto;
import com.pullanner.domain.user.service.UserService;
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
        return userService.findByEmail(email); // TODO : 매번 이메일로 디피 조회해야하나? -> 어짜피 사용자가 새로고침하지 않는 이상 한번만 호출하닌까 괜찮지 않나?
    }

    @PatchMapping("/api/users/{id}")
    public UserResponseDto update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequestDto request) {
        return userService.update(id, request);
    }
}
