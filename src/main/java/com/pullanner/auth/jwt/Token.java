package com.pullanner.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public abstract class Token {

    @Getter
    protected String token;
    protected Key secretKey;

    @Getter
    @RequiredArgsConstructor
    enum ClaimKey {
        USER_ROLE("role"),
        TOKEN_TYPE("tokenType");

        private final String keyName;
    }

    protected Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public boolean validateExpirationOfToken() {
        try {
            return !getClaims(token)
                .getExpiration()
                .before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUserInfo() {
        try {
            return getClaims(token)
                .getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (JwtException e) {
            throw new IllegalStateException("유효하지 않은 토큰입니다.");
        }
    }
}
