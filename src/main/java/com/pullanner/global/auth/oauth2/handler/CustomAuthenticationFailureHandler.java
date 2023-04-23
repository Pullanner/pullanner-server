package com.pullanner.global.auth.oauth2.handler;

import com.pullanner.global.ApiResponseCode;
import com.pullanner.global.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException {
        response.setStatus(ApiResponseCode.OAUTH2_LOGIN_FAIL.getStatusCode());
        ServletUtil.setApiResponse(response, ApiResponseCode.OAUTH2_LOGIN_FAIL);
    }
}
