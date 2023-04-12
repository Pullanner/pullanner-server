package com.pullanner.auth.oauth2.handler;

import static com.pullanner.auth.jwt.utils.TokenUtil.*;

import com.pullanner.auth.jwt.service.JwtService;
import com.pullanner.web.dto.common.AuthenticationResponse;
import com.pullanner.web.utils.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        String accessToken = jwtService.createAccessToken(authentication);
        String refreshToken = jwtService.createRefreshToken(authentication);

        addAccessTokenHeader(response, accessToken);
        addRefreshTokenCookie(response, refreshToken);
        ServletUtil.setResponseBody(response, AuthenticationResponse.OAUTH2_LOGIN_SUCCESS);
    }
}
