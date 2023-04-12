package com.pullanner.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pullanner.global.AuthenticationResponse;
import com.pullanner.global.ApiResponseMessage;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.springframework.http.MediaType;

public class ServletUtil {

    public static void setResponseBody(HttpServletResponse response, AuthenticationResponse authenticationResponse) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(authenticationResponse.getSc());
        response.getWriter().write(
            Objects.requireNonNull(
                mapper.writeValueAsString(
                    new ApiResponseMessage(authenticationResponse.getCode(), authenticationResponse.getMessage())
                )
            )
        );
    }
}
