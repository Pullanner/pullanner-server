package com.pullanner.exception.user;

public class InvalidMailAuthorizationCodeException extends RuntimeException {

    public InvalidMailAuthorizationCodeException() {
        super("회원 탈퇴 처리를 위한 인증 번호가 일치하지 않습니다.");
    }
}
