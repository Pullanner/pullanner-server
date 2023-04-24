package com.pullanner.global.auth.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginSuccessResponse {

    private Long userId;
    private String name;
    private String nickName;
    private String email;
    private String picture;
    private String accessToken;

}
