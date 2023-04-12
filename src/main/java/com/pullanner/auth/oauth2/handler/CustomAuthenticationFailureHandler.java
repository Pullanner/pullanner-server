package com.pullanner.auth.oauth2.handler;

import com.pullanner.web.dto.common.AuthenticationResponse;
import com.pullanner.web.utils.ServletUtil;
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
        ServletUtil.setResponseBody(response, AuthenticationResponse.OAUTH2_LOGIN_FAIL);
    }
}
