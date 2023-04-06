package com.pullanner.auth.handler;

import static com.pullanner.auth.handler.ServletAuthenticationUtils.*;

import com.pullanner.auth.jwt.Token;
import com.pullanner.auth.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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

        addAccessTokenHeader(response, accessToken);
        addRefreshTokenCookie(response, refreshToken);
        setSuccessResponseBody(response);
    }

    private String getOAuth2UserEmail(Authentication authentication) {
        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = defaultOAuth2User.getAttributes();
        return (String) attributes.get("email");
    }
}
