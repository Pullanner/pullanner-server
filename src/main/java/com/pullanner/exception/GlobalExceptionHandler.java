package com.pullanner.exception;

import com.pullanner.web.ApiResponseCode;
import com.pullanner.web.ApiResponseMessage;
import com.pullanner.web.ApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseMessage> handleGlobalException(Exception e) {
        log.error("", e);
        return ApiUtil.getResponseEntity(ApiResponseCode.SERVER_INTERNAL_ERROR);
    }
}
