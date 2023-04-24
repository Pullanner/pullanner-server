package com.pullanner.domain.user.controller;

import com.pullanner.domain.user.dto.UserResponseDto;
import com.pullanner.domain.user.dto.UserUpdateRequestDto;
import com.pullanner.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/api/users/{id}")
    public UserResponseDto find(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PatchMapping("/api/users/{id}")
    public UserResponseDto update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequestDto request) {
        return userService.update(id, request);
    }
}
