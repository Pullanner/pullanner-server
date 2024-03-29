package com.pullanner.web.controller.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class UserNicknameUpdateRequest {

    @Length(min = 2, max = 8)
    private String nickname;
}
