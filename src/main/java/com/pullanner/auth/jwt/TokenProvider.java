package com.pullanner.auth.jwt;

import org.springframework.security.core.Authentication;

public interface TokenProvider<T> {

    T createToken(String email);

    T convertToObject(String token);
}
