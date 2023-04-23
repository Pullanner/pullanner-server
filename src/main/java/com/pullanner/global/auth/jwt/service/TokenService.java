package com.pullanner.global.auth.jwt.service;

import com.pullanner.global.auth.jwt.exception.InvalidTokenException;
import com.pullanner.global.auth.oauth2.dto.OAuth2UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;

public class TokenService {

    protected static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    protected static final Key SECRET_KEY = Keys.secretKeyFor(signatureAlgorithm);

    protected static final String PROVIDER = "provider";
    protected static final String AUTHORITIES = "authorities";
    protected static final String EMAIL = "email";

    protected Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }

    protected String makeToken(OAuth2UserInfo oAuth2UserInfo, List<String> authorities, long expiration) {
        String id = oAuth2UserInfo.getId();
        String provider = oAuth2UserInfo.getProvider().toString();
        String email = oAuth2UserInfo.getEmail();

        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setHeaderParam(Header.COMPRESSION_ALGORITHM, signatureAlgorithm)
            .setSubject(id)
            .claim(PROVIDER, provider)
            .claim(AUTHORITIES, authorities)
            .claim(EMAIL, email)
            .setIssuedAt(new Date())
            .setExpiration(new Date(expiration))
            .signWith(SECRET_KEY)
            .compact();
    }

    protected String remakeToken(String token, long expiration) {
        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setClaims(getClaims(token))
            .setIssuedAt(new Date())
            .setExpiration(new Date(expiration))
            .signWith(SECRET_KEY)
            .compact();
    }
}
