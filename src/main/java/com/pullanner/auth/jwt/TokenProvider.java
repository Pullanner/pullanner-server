package com.pullanner.auth.jwt;

import org.springframework.security.core.Authentication;

public interface TokenProvider<T> {

    T createToken(Authentication authentication);

    T convertToObject(String token);
}
