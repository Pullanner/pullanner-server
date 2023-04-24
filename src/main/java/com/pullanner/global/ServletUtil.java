package com.pullanner.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pullanner.global.auth.jwt.dto.AccessTokenResponse;
import com.pullanner.global.auth.jwt.exception.InvalidTokenException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.util.StringUtils;

public class ServletUtil {

    public static final String ACCESS_TOKEN_TYPE = "Bearer ";
    private static final String REFRESH_TOKEN_ID_COOKIE_NAME = "renew";
    private static final int REFRESH_TOKEN_ID_DURATION = 60 * 10; // 10 minutes -> seconds

    public static String parseAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(ACCESS_TOKEN_TYPE)) {
            return bearerToken.substring(ACCESS_TOKEN_TYPE.length());
        }

        throw new InvalidTokenException();
    }

    public static String parseRefreshTokenId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN_ID_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }

        throw new InvalidTokenException();
    }

    public static void setAccessTokenResponse(HttpServletResponse response, String accessToken) throws IOException {
        setResponseHeader(response, HttpStatus.OK.value());
        setResponseBody(response, new AccessTokenResponse(accessToken));
    }

    public static void addRefreshTokenCookie(HttpServletResponse response, String token) {
        response.addHeader("Set-Cookie", getRefreshTokenCookieInfo(token));
    }

    // TODO : https 적용 시 Secure 설정
    private static String getRefreshTokenCookieInfo(String token) {
        return ResponseCookie.from(REFRESH_TOKEN_ID_COOKIE_NAME, token)
            .path("/api")
            .httpOnly(true)
            .sameSite("Strict")
            .maxAge(REFRESH_TOKEN_ID_DURATION)
            .build()
            .toString();
    }

    public static void setApiResponse(HttpServletResponse response, ApiResponseCode apiResponseCode) throws IOException {
        setResponseHeader(response, apiResponseCode.getStatusCode());
        setResponseBody(response, new ApiResponseMessage(apiResponseCode.getCode(), apiResponseCode.getMessage()));
    }

    private static void setResponseHeader(HttpServletResponse response, int statusCode) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
    }

    private static void setResponseBody(HttpServletResponse response, Object value)
        throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        response.getWriter().write(
            Objects.requireNonNull(
                mapper.writeValueAsString(value)
            )
        );
    }
}