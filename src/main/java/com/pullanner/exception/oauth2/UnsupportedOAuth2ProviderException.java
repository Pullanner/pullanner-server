package com.pullanner.exception.oauth2;

import org.springframework.security.core.AuthenticationException;

public class UnsupportedOAuth2ProviderException extends AuthenticationException {

    public UnsupportedOAuth2ProviderException() {
        super("지원되지 않는 OAuth Provider 입니다.");
    }

}
