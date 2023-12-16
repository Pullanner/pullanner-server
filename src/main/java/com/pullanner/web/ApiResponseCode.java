package com.pullanner.web;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiResponseCode {

    OAUTH2_LOGIN_FAIL("A01", 401, "로그인이 실패했습니다."),
    TOKEN_INVALID("A02", 401, "유효하지 않은 토큰입니다."),
    TOKEN_HACKED("A03", 401, "계정이 도용된 것으로 의심됩니다."),
    TOKEN_REFRESHED("A04", 200, "액세스 토큰이 재발급되었습니다."),

    USER_NOT_FOUND("U01", 404, "존재하지 않는 회원입니다."),
    USER_PROFILE_IMAGE_UPLOAD_FAIL("U02", 500, "프로필 이미지 업로드에 실패했습니다."),
    USER_DUPLICATE_NICKNAME("U03", 400, "이미 존재하는 닉네임입니다."),
    USER_NOT_DUPLICATE_NICKNAME("U04", 200, "사용 가능한 닉네임입니다."),
    USER_INVALID_NICKNAME("U05", 400, "유효하지 않는 닉네임입니다."),
    USER_EMAIL_SENDING_SUCCESS("U06", 200, "사용자의 이메일로 인증 코드가 발송되었습니다."),
    USER_INVALID_MAIL_AUTHORIZATION_CODE("U07", 401, "유효하지 않은 인증 코드입니다."),
    USER_DELETED_SUCCESS("U08", 200, "사용자의 회원 정보가 삭제되었습니다."),
    USER_LOGOUT_SUCCESS("U09", 200, "로그아웃이 정상적으로 처리되었습니다."),
    USER_ALREADY_SENT_AUTHORIZATION_CODE("U10", 400, "이미 사용자의 이메일로 인증 코드가 발송되었습니다."),
    USER_LEVEL_NOT_SUPPORTED("U11", 500, "해당 사용자의 경험치로 레벨을 산출하는 것이 지원되지 않습니다."),

    USER_WORKOUT_UPDATED("UW01", 200, "사용자가 할 수 있는 철봉 동작 정보가 등록(수정)되었습니다."),

    PLAN_SAVED("P01", 200, "철봉 운동 계획이 등록되었습니다."),
    PLAN_SAVE_DATE_INVALID("P02", 400, "오늘 이후의 날짜로만 계획을 등록할 수 있습니다."),
    PLAN_UPDATED("P03", 200, "철봉 운동 계획이 수정되었습니다."),
    PLAN_UPDATE_DATETIME_INVALID("P04", 400, "현재 시간 보다 이전 시간으로는 계획을 생성하거나 수정할 수 없습니다."),
    PLAN_ACCESS_NO_AUTHORITY("P05", 200, "철봉 운동 계획을 수정할 권한이 없습니다."),
    PLAN_NOT_FOUNDED("P06", 404, "등록된 철봉 운동 계획이 없습니다."),
    PLAN_WORKOUT_NOT_FOUNDED("P07", 404, "해당 철봉 운동 계획에 등록된 활동이 없습니다."),
    PLAN_CHECKED("P08", 200, "해당 철봉 운동 계획의 달성 여부를 체크했습니다."),
    PLAN_DELETED("P09", 200, "철봉 운동 계획이 삭제되었습니다."),
    PLAN_COMPLETED_NOT_CHANGED("P10", 400, "완료된 철봉 운동 계획은 변경할 수 없습니다."),
    PLAN_WORKOUT_DONE("P11", 400, "완료한 철봉 운동 활동은 변경할 수 없습니다."),
    PLAN_INVALID_SAVE_REQUEST("P12", 400, "철봉 운동 계획 유형에 맞지 않는 철봉 운동 계획 등록 요청입니다."),

    ARTICLE_DELETE_COMPLETED("B01", 200, "게시글이 삭제되었습니다."),

    REQUEST_RESOURCE_NOT_FOUND("R01", 404, "요청하신 데이터가 존재하지 않습니다."),

    SERVER_INTERNAL_ERROR("G01", 500, "현재 서버 내부에서 에러가 발생했습니다.");

    private final String code;
    private final int httpStatusCode;
    private final String message;
}
