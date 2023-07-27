package com.pullanner.web;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiResponseCode {

    OAUTH2_LOGIN_FAIL("A02", 401, "로그인이 실패했습니다."),
    TOKEN_INVALID("A03", 401, "유효하지 않은 토큰입니다."),
    TOKEN_HACKED("A04", 401, "계정이 도용된 것으로 의심됩니다."),
    TOKEN_REFRESHED("A05", 200, "액세스 토큰이 재발급되었습니다."),

    USER_NOT_FOUND("U01", 404, "존재하지 않는 회원입니다."),
    USER_PROFILE_IMAGE_UPLOAD_FAIL("U02", 500, "프로필 이미지 업로드에 실패했습니다."),
    USER_DUPLICATE_NICKNAME("U03", 400, "이미 존재하는 닉네임입니다."),
    USER_NOT_DUPLICATE_NICKNAME("U04", 200, "사용 가능한 닉네임입니다."),
    USER_INVALID_NICKNAME("U05", 400, "유효하지 않는 닉네임입니다."),
    USER_EMAIL_SENDING_SUCCESS("U06", 200, "사용자의 이메일로 인증 코드가 발송되었습니다."),
    USER_INVALID_MAIL_AUTHORIZATION_CODE("U07", 401, "유효하지 않은 인증 코드입니다."),
    USER_DELETED_SUCCESS("U08", 200, "사용자의 회원 정보가 삭제되었습니다."),
    USER_LOGOUT_SUCCESS("U09", 200, "로그아웃이 정상적으로 처리되었습니다."),

    ARTICLE_DELETE_COMPLETED("B01", 200, "게시글이 삭제되었습니다."),

    REQUEST_RESOURCE_NOT_FOUND("R1", 404, "요청하신 데이터가 존재하지 않습니다."),

    SERVER_INTERNAL_ERROR("G01", 500, "현재 서버 내부에서 에러가 발생했습니다.");

    private final String code;
    private final int httpStatusCode;
    private final String message;
}
