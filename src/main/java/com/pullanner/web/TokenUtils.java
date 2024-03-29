package com.pullanner.web;

import com.pullanner.exception.token.InvalidTokenException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

public class TokenUtils {

    private static final String ACCESS_TOKEN_HEADER_TYPE = "Bearer ";

    private static final String ACCESS_TOKEN_COOKIE_NAME = "auth";
    private static final String REFRESH_TOKEN_ID_COOKIE_NAME = "renew";

    private static final int ACCESS_TOKEN_COOKIE_DURATION = 10; // 10 seconds
    private static final int REFRESH_TOKEN_ID_COOKIE_DURATION = 600; // 600 seconds (= 10 minutes)

    public static String parseAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(ACCESS_TOKEN_HEADER_TYPE)) {
            return bearerToken.substring(ACCESS_TOKEN_HEADER_TYPE.length());
        }

        throw new InvalidTokenException();
    }

    public static String parseRefreshTokenId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new InvalidTokenException();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN_ID_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }

        throw new InvalidTokenException();
    }

    public static Cookie getAccessTokenCookie(String accessToken) {
        Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(false);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setAttribute("SameSite", "Strict");
        accessTokenCookie.setMaxAge(ACCESS_TOKEN_COOKIE_DURATION);

        return accessTokenCookie;
    }

    public static Cookie getRefreshTokenIdCookie(String refreshTokenId) {
        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_ID_COOKIE_NAME, refreshTokenId);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setAttribute("SameSite", "Strict");
        refreshTokenCookie.setMaxAge(REFRESH_TOKEN_ID_COOKIE_DURATION);

        return refreshTokenCookie;
    }
}
