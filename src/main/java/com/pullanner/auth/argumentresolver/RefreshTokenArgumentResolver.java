package com.pullanner.auth.argumentresolver;

import com.pullanner.auth.exception.InvalidTokenException;
import com.pullanner.auth.jwt.RefreshToken;
import com.pullanner.auth.jwt.RefreshTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class RefreshTokenArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh";

    private final RefreshTokenProvider refreshTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean hasRefreshTokenType = RefreshToken.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginUserAnnotation && hasRefreshTokenType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String refreshToken = getRefreshToken(request);

        return refreshTokenProvider.convertToObject(refreshToken);
    }

    private String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }

        throw new InvalidTokenException("refresh 토큰이 존재하지 않습니다.");
    }

}
