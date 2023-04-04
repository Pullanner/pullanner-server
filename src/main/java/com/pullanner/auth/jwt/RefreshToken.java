package com.pullanner.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;

public class RefreshToken extends Token {

    public RefreshToken(String token, Key secretKey) {
        super.token = token;
        super.secretKey = secretKey;
    }

    RefreshToken(Authentication authentication, Key secretKey, SignatureAlgorithm signatureAlgorithm, long duration) {
        super.secretKey = secretKey;
        super.token = compactToken(authentication, signatureAlgorithm, new Date(System.currentTimeMillis() + duration));
    }

    private String compactToken(Authentication authentication, SignatureAlgorithm signatureAlgorithm, Date expiration) {
        return Jwts.builder()
            .setSubject(String.valueOf(authentication.getPrincipal()))
            .setClaims(makeClaims(authentication))
            .setExpiration(expiration)
            .signWith(secretKey, signatureAlgorithm)
            .compact();
    }

    private Map<String, Object> makeClaims(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ClaimKey.TOKEN_TYPE.getKeyName(), TokenType.REFRESH.name());
        claims.put(ClaimKey.USER_ROLE.getKeyName(), authentication.getAuthorities());
        return claims;
    }
}
