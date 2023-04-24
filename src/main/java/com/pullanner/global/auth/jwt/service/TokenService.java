package com.pullanner.global.auth.jwt.service;

import com.pullanner.global.auth.jwt.exception.InvalidTokenException;
import com.pullanner.global.auth.oauth2.dto.OAuth2UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;

public class TokenService {

    protected static final Key SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(System.getenv("JWT_SECRET")));
    protected static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    protected static final String PROVIDER = "provider";
    protected static final String AUTHORITIES = "authorities";
    protected static final String EMAIL = "email";
    protected static final String INVALIDATE = "invalidate";

    protected static final long ACCESS_TOKEN_DURATION = 2 * 60_000; // 2 minutes
    protected static final long REFRESH_TOKEN_DURATION = 10 * 60_000; // 10 minutes -> milliseconds

    protected Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (JwtException e) {
            e.printStackTrace();
            throw new InvalidTokenException();
        }
    }

    protected String makeToken(OAuth2UserInfo oAuth2UserInfo, List<String> authorities, long expiration) {
        String id = String.valueOf(oAuth2UserInfo.getId());
        String provider = oAuth2UserInfo.getProvider().toString();
        String email = oAuth2UserInfo.getEmail();

        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setSubject(id)
            .claim(PROVIDER, provider)
            .claim(AUTHORITIES, authorities)
            .claim(EMAIL, email)
            .claim(INVALIDATE, false)
            .setIssuedAt(new Date())
            .setExpiration(new Date(expiration))
            .signWith(SECRET_KEY, SIGNATURE_ALGORITHM)
            .compact();
    }

    protected String remakeToken(String token, long expiration) {
        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setClaims(getClaims(token))
            .setIssuedAt(new Date())
            .setExpiration(new Date(expiration))
            .signWith(SECRET_KEY, SIGNATURE_ALGORITHM)
            .compact();
    }
}
