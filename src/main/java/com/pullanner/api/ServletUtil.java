package com.pullanner.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.springframework.http.MediaType;

public class ServletUtil {

    // HttpServletResponse 를 이용한 응답 헤더와 응답 바디 설정
    public static void setApiResponse(HttpServletResponse response, ApiResponseCode apiResponseCode) throws IOException {
        setResponseHeader(response, apiResponseCode.getHttpStatusCode());
        setResponseBody(response, ApiResponseMessage.of(apiResponseCode.getCode(), apiResponseCode.getMessage()));
    }

    // 응답 헤더 설정
    private static void setResponseHeader(HttpServletResponse response, int statusCode) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
    }

    // 응답 바디 설정
    private static void setResponseBody(HttpServletResponse response, Object value)
        throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        response.getWriter().write(
            Objects.requireNonNull(
                mapper.writeValueAsString(value)
            )
        );
    }
}
