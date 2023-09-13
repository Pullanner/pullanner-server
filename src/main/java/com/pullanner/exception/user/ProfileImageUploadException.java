package com.pullanner.exception.user;

import com.pullanner.web.ApiResponseCode;

public class ProfileImageUploadException extends RuntimeException {

    public ProfileImageUploadException() {
        super(ApiResponseCode.USER_PROFILE_IMAGE_UPLOAD_FAIL.getMessage());
    }
}
