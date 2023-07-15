package com.pullanner.web;

import org.springframework.http.ResponseEntity;

public class ApiUtil {

    public static ResponseEntity<ApiResponseMessage> getResponseEntity(ApiResponseCode responseCode) {
        return ResponseEntity.status(responseCode.getHttpStatusCode())
            .body(
                ApiResponseMessage.of(responseCode.getCode(), responseCode.getMessage())
            );
    }

}
