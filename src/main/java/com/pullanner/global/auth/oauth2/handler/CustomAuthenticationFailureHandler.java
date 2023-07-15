package com.pullanner.global.auth.oauth2.handler;

import com.pullanner.api.ApiResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.pullanner.global.servlet.ServletUtil.setApiResponse;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException {
        setApiResponse(response, ApiResponseCode.OAUTH2_LOGIN_FAIL);
    }
}
