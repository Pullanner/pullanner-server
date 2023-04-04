package com.pullanner.web.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiResponse {

    LOGIN_SUCCESS("R01", 200, "로그인이 완료되었습니다."),
    LOGIN_FAIL("R02", 401, "로그인이 실패했습니다.");

    private final String code;
    private final int sc;
    private final String message;
}
