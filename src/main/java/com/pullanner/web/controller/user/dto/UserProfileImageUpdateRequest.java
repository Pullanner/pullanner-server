package com.pullanner.web.controller.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileImageUpdateRequest {

    @NotEmpty
    private String profileImage;
}
