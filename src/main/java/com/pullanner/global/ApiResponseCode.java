package com.pullanner.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiResponseCode {

    OAUTH2_LOGIN_SUCCESS("A01", 200, "로그인이 완료되었습니다."),
    OAUTH2_LOGIN_FAIL("A02", 401, "로그인이 실패했습니다."),
    TOKEN_INVALID("A03", 401, "유효하지 않은 토큰입니다."),
    TOKEN_HACKED("A04", 401, "토큰 도용이 의심됩니다."),

    USER_UPDATE_SUCCESS("U01", 200, "회원 정보 수정이 완료되었습니다.");

    private final String code;
    private final int statusCode;
    private final String message;
}