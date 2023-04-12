package com.pullanner.web.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthenticationResponse {

    OAUTH2_LOGIN_SUCCESS("R01", 200, "로그인이 완료되었습니다."),
    OAUTH2_LOGIN_FAIL("R02", 401, "로그인이 실패했습니다."),
    INVALID_TOKEN("R03", 401, "유효하지 않은 토큰입니다."),
    TOKEN_REISSUE("R04", 200, "토큰이 재발급되었습니다.");

    private final String code;
    private final int sc;
    private final String message;
}
