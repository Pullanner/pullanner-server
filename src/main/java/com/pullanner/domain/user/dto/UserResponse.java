package com.pullanner.domain.user.dto;

import com.pullanner.domain.user.entity.User;
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
    private Integer journalCount;

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
            .profileImage(user.getPicture())
            .oauthProvider(user.getProvider().name())
            .build();
    }
}
