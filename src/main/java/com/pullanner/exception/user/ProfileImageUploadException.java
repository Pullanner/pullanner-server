package com.pullanner.exception.user;

public class ProfileImageUploadException extends RuntimeException {

    public ProfileImageUploadException() {
        super("S3 에 파일 업로드를 실패했습니다.");
    }
}
