package com.pullanner.domain.user.dto;

import com.pullanner.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseDto {

    private Long userId;
    private String name;
    private String nickName;
    private String email;
    private String picture;

    private UserResponseDto(Long userId, String name, String nickName, String email, String picture) {
        this.userId = userId;
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.picture = picture;
    }

    public static UserResponseDto from(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getNickName(), user.getEmail(), user.getPicture());
    }
}
