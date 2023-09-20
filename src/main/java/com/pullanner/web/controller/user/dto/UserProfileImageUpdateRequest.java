package com.pullanner.web.controller.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class UserProfileImageUpdateRequest {

    @NotNull
    MultipartFile profileImage;
}
