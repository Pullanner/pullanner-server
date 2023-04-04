package com.pullanner.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pullanner.web.dto.common.ApiResponse;
import com.pullanner.web.dto.common.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException {
        setFailResponseBody(response);
    }

    private void setFailResponseBody(HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(ApiResponse.LOGIN_FAIL.getSc());
        response.getWriter().write(Objects.requireNonNull(mapper.writeValueAsString(
                    new ResponseMessage(
                        ApiResponse.LOGIN_FAIL.getCode(), ApiResponse.LOGIN_FAIL.getMessage()
                    )
                )
            )
        );
    }
}
