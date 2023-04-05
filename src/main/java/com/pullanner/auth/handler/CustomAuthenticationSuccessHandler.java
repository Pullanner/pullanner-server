package com.pullanner.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pullanner.auth.jwt.Token;
import com.pullanner.auth.service.TokenService;
import com.pullanner.web.dto.common.ApiResponse;
import com.pullanner.web.dto.common.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        String email = getOAuth2UserEmail(authentication);

        Token accessToken = tokenService.createAccessToken(email);
        Token refreshToken = tokenService.createRefreshToken(email);

        response.addHeader("Token", accessToken.getToken());
        response.addHeader("Set-Cookie", getRefreshTokenCookieInfo(refreshToken));

        setSuccessResponseBody(response);
    }

    private String getOAuth2UserEmail(Authentication authentication) {
        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = defaultOAuth2User.getAttributes();
        return (String) attributes.get("email");
    }

    private String getRefreshTokenCookieInfo(Token refreshToken) {
        return ResponseCookie.from("refresh", refreshToken.getToken())
            .path("/api")
            .httpOnly(true)
            .sameSite("Strict")
            .build()
            .toString();
    }

    private void setSuccessResponseBody(HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(ApiResponse.LOGIN_SUCCESS.getSc());
        response.getWriter().write(Objects.requireNonNull(mapper.writeValueAsString(
                    new ResponseMessage(
                        ApiResponse.LOGIN_SUCCESS.getCode(), ApiResponse.LOGIN_SUCCESS.getMessage()
                    )
                )
            )
        );
    }
}
