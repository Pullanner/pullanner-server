package com.pullanner.web.controller.user.dto;

import com.pullanner.domain.user.User;
import lombok.Builder;
import lombok.Getter;

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

    @Builder
    private UserResponse(Long userId, String name, String nickname, String email,
        String profileImage, String oauthProvider) {
        this.userId = userId;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.oauthProvider = oauthProvider;
    }

    public static UserResponse from(User user) {
        return UserResponse.builder()
            .userId(user.getId())
            .name(user.getName())
            .nickname(user.getNickName())
            .email(user.getEmail())
            .profileImage(user.getProfileImageUrl())
            .oauthProvider(user.getProvider().name())
            .build();
    }
}
