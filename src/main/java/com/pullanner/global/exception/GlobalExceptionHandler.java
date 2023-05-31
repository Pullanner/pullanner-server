package com.pullanner.global.exception;

import com.pullanner.global.api.ApiResponseCode;
import com.pullanner.global.api.ApiResponseMessage;
import com.pullanner.global.api.ApiUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseMessage> handleMethodArgumentTypeMismatchException(Exception e) {
        e.printStackTrace();
        return ApiUtil.getResponseEntity(ApiResponseCode.SERVER_INTERNAL_ERROR);
    }
}
