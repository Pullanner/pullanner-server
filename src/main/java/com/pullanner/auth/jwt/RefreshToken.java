package com.pullanner.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RefreshToken extends Token {

    public RefreshToken(String token, Key secretKey) {
        super.token = token;
        super.secretKey = secretKey;
    }

    RefreshToken(String email, Key secretKey, SignatureAlgorithm signatureAlgorithm, long duration) {
        super.secretKey = secretKey;
        super.signatureAlgorithm = signatureAlgorithm;
        Date expiration = new Date(duration);
        super.token = compactToken(email, signatureAlgorithm, expiration);
        super.expiration = expiration;
    }

    /**
     *  리프레쉬 토큰을 이용하여 액세스 토큰을 재발급 받을 때마다 리프레쉬 토큰 값 갱신
     *  이떄, 기존 리프레쉬 토큰의 유효기간은 변하지 않음
     *  이를 통해 리프레쉬 토큰 탈취 시 피해 파급 최소화
     */
    public void renewToken() {
        super.token = compactToken(getUserEmail(), signatureAlgorithm, expiration);
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
