package com.pullanner.auth.argumentresolver;

import com.pullanner.auth.exception.InvalidTokenException;
import com.pullanner.auth.jwt.AccessToken;
import com.pullanner.auth.jwt.AccessTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

// TODO : JWT 필터 덕에 이 클래스 필요없지 않은지?
@RequiredArgsConstructor
public class AccessTokenArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String ACCESS_TOKEN_TYPE = "Bearer ";
    private final AccessTokenProvider accessTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean hasAccessTokenType = AccessToken.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginUserAnnotation && hasAccessTokenType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        return accessTokenProvider.convertToObject(getAccessToken(request));
    }

    public String getAccessToken(HttpServletRequest request) {
        String valueOfAuthorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (valueOfAuthorizationHeader == null || !valueOfAuthorizationHeader.startsWith(ACCESS_TOKEN_TYPE)) {
            throw new InvalidTokenException("access 토큰이 존재하지 않습니다.");
        }

        return valueOfAuthorizationHeader.substring(ACCESS_TOKEN_TYPE.length());
    }
}
