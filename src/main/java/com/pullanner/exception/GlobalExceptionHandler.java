package com.pullanner.exception;

import com.pullanner.api.ApiResponseCode;
import com.pullanner.api.ApiResponseMessage;
import com.pullanner.api.ApiUtil;
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
