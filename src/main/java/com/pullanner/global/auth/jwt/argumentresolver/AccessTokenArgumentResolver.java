package com.pullanner.global.auth.jwt.argumentresolver;

import static com.pullanner.global.auth.jwt.utils.TokenUtil.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AccessTokenArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAccessTokenAnnotation = parameter.hasParameterAnnotation(AccessToken.class);
        boolean hasAccessTokenType = String.class.isAssignableFrom(parameter.getParameterType());

        return hasAccessTokenAnnotation && hasAccessTokenType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        return parseAccessToken(request);
    }
}
