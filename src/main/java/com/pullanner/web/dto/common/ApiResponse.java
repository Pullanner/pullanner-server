package com.pullanner.web.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiResponse {

    OAUTH2_LOGIN_SUCCESS("R01", 200, "로그인이 완료되었습니다."),
    OAUTH2_LOGIN_FAIL("R02", 401, "로그인이 실패했습니다."),
    TOKEN_REISSUE("R03", 200, "토큰이 재발급되었습니다.");

    private final String code;
    private final int sc;
    private final String message;
}
