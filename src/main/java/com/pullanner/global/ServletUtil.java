package com.pullanner.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.springframework.http.MediaType;

public class ServletUtil {

    public static void setResponseBody(HttpServletResponse response, ApiResponseCode apiResponseCode) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        response.setStatus(apiResponseCode.getSc());

        response.getWriter().write(
            Objects.requireNonNull(
                mapper.writeValueAsString(
                    new ApiResponseMessage(apiResponseCode.getCode(), apiResponseCode.getMessage())
                )
            )
        );
    }
}
