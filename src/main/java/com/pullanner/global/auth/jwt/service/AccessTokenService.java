package com.pullanner.global.auth.jwt.service;

import com.pullanner.global.auth.jwt.dto.JwtAuthenticationResult;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenService extends TokenService {

    private static final long ACCESS_TOKEN_DURATION = 2 * 60_000; // 2 minutes
    private static final String AUTHORITIES = "authorities";

    public String createAccessToken(String userId) {
        return makeToken(userId, System.currentTimeMillis() + ACCESS_TOKEN_DURATION);
    }

    public String renewAccessToken(String token) {
        return remakeToken(token, System.currentTimeMillis() + ACCESS_TOKEN_DURATION);
    }

    public Claims validateAndGetClaims(String token) {
        return getClaims(token);
    }

    public JwtAuthenticationResult getJwtAuthenticationResult(Claims claims) {
        return new JwtAuthenticationResult(
            Long.parseLong(claims.getSubject()),
            (List<SimpleGrantedAuthority>) claims.get(AUTHORITIES, List.class).stream()
                .map(authority-> new SimpleGrantedAuthority((String) authority))
                .collect(Collectors.toList())
            );
    }
}
