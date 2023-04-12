package com.pullanner.global.auth.jwt.utils;

import com.pullanner.global.auth.jwt.exception.InvalidTokenException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.util.StringUtils;

public class TokenUtil {

    private static final String ACCESS_TOKEN_HEADER = "Token";
    public static final String ACCESS_TOKEN_TYPE = "Bearer ";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh";

    public static String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(ACCESS_TOKEN_TYPE)) {
            return bearerToken.substring(ACCESS_TOKEN_TYPE.length());
        }

        throw new InvalidTokenException("There is no access token...");
    }

    public static String parseRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }

        throw new InvalidTokenException("refresh 토큰이 존재하지 않습니다.");
    }

    public static void addAccessTokenHeader(HttpServletResponse response, String token) {
        response.addHeader(ACCESS_TOKEN_HEADER, token);
    }

    public static void addRefreshTokenCookie(HttpServletResponse response, String token) {
        response.addHeader("Set-Cookie", getRefreshTokenCookieInfo(token));
    }

    // TODO : https 적용 시 Secure 설정
    private static String getRefreshTokenCookieInfo(String token) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, token)
            .path("/api")
            .httpOnly(true)
            .sameSite("Strict")
            .build()
            .toString();
    }
}
