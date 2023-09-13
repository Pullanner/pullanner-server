package com.pullanner.exception.badge;

public class BadgeNotFoundedException extends RuntimeException {

    public BadgeNotFoundedException() {
        super("존재하지 않는 뱃지입니다.");
    }
}
