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

    RefreshToken(String email, Key secretKey, SignatureAlgorithm signatureAlgorithm, long duration) {
        super.secretKey = secretKey;
        super.token = compactToken(email, signatureAlgorithm, new Date(System.currentTimeMillis() + duration));
    }

    private String compactToken(String email, SignatureAlgorithm signatureAlgorithm, Date expiration) {
        return Jwts.builder()
            .setSubject(email)
            .setClaims(makeClaims())
            .setExpiration(expiration)
            .signWith(secretKey, signatureAlgorithm)
            .compact();
    }

    private Map<String, Object> makeClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ClaimKey.TOKEN_TYPE.getKeyName(), TokenType.REFRESH.name());
        return claims;
    }
}
