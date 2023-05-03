package com.pullanner.global.auth.oauth2.handler;

import static com.pullanner.global.ServletUtil.*;

import com.pullanner.global.auth.jwt.service.AccessTokenService;
import com.pullanner.global.auth.jwt.service.RefreshTokenService;
import com.pullanner.global.auth.oauth2.dto.OAuth2UserInfo;
import com.pullanner.global.auth.oauth2.utils.OAuth2UserInfoUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoUtil.getOAuth2UserInfo(
            (OAuth2AuthenticationToken) authentication);

        String userId = oAuth2UserInfo.getUserId();

        String accessToken = accessTokenService.createAccessToken(userId);
        String refreshTokenId = refreshTokenService.createRefreshToken(userId);

        setLoginSuccessResponse(response, oAuth2UserInfo, accessToken);
        addRefreshTokenCookie(response, refreshTokenId);
    }
}
