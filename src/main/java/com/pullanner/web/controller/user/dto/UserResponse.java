package com.pullanner.web.controller.user.dto;

import com.pullanner.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserResponse {

    private Long userId;
    private String name;
    private String nickname;
    private String email;
    private String profileImage;
    private String oauthProvider;
    private Integer level;
    private Integer experiencePoint;
    private LocalDateTime joinDate;

    public static UserResponse from(User user) {
        return UserResponse.builder()
            .userId(user.getId())
            .name(user.getName())
            .nickname(user.getNickName())
            .email(user.getEmail())
            .profileImage(user.getProfileImageUrl())
            .oauthProvider(user.getProvider().name())
            .level(user.getLevel())
            .experiencePoint(user.getExperiencePoint())
            .joinDate(user.getCreatedDate())
            .build();
    }
}
