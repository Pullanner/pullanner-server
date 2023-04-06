package com.pullanner.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pullanner.auth.jwt.Token;
import com.pullanner.web.dto.common.ApiResponse;
import com.pullanner.web.dto.common.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.util.StringUtils;

public class ServletAuthenticationUtils {

    private static final String ACCESS_TOKEN_HEADER = "Token";
    public static final String ACCESS_TOKEN_TYPE = "Bearer ";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh";

    public static String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(ACCESS_TOKEN_TYPE)) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public static void setSuccessResponseBody(HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(ApiResponse.OAUTH2_LOGIN_SUCCESS.getSc());
        response.getWriter().write(Objects.requireNonNull(mapper.writeValueAsString(
                    new ResponseMessage(
                        ApiResponse.OAUTH2_LOGIN_SUCCESS.getCode(), ApiResponse.OAUTH2_LOGIN_SUCCESS.getMessage()
                    )
                )
            )
        );
    }

    public static void setFailResponseBody(HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(ApiResponse.OAUTH2_LOGIN_FAIL.getSc());
        response.getWriter().write(Objects.requireNonNull(mapper.writeValueAsString(
                    new ResponseMessage(
                        ApiResponse.OAUTH2_LOGIN_FAIL.getCode(), ApiResponse.OAUTH2_LOGIN_FAIL.getMessage()
                    )
                )
            )
        );
    }

    public static void addAccessTokenHeader(HttpServletResponse response, Token accessToken) {
        response.addHeader(ACCESS_TOKEN_HEADER, accessToken.getToken());
    }

    public static void addRefreshTokenCookie(HttpServletResponse response, Token refreshToken) {
        response.addHeader("Set-Cookie", getRefreshTokenCookieInfo(refreshToken));
    }

    // TODO : https 적용 시 Secure 설정
    private static String getRefreshTokenCookieInfo(Token refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken.getToken())
            .path("/api")
            .httpOnly(true)
            .sameSite("Strict")
            .build()
            .toString();
    }
}
