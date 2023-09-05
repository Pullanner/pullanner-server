package com.pullanner.exception.user;

public class UserNotFoundedException extends RuntimeException {

    public UserNotFoundedException(Long userId) {
        super("식별 번호가 " + userId + "에 해당되는 사용자가 없습니다.");
    }
}
