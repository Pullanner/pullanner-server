package com.pullanner.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @Length(min = 6, max = 15)
    private String nickName;
}
