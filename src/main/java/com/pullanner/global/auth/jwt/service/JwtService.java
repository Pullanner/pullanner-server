package com.pullanner.global.auth.jwt.service;

import com.pullanner.global.auth.jwt.dto.JwtAuthenticationResult;
import com.pullanner.global.auth.jwt.repository.RefreshTokenRepository;
import com.pullanner.global.auth.jwt.exception.InvalidTokenException;
import com.pullanner.global.auth.oauth2.dto.OAuth2UserInfo;
import com.pullanner.global.auth.oauth2.utils.OAuth2UserInfoUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final RefreshTokenRepository refreshTokenRepository;

    // 서버 실행 시 secretKy 변경
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private static final String PROVIDER = "provider";
    private static final String AUTHORITIES = "authorities";
    private static final String EMAIL = "email";

    private static final long REFRESH_TOKEN_DURATION = 10 * 60_000; // 10 minutes
    private static final long ACCESS_TOKEN_DURATION = 2 * 60_000; // 2 minutes

    public String createAccessToken(Authentication authentication) {
        return makeToken(authentication, System.currentTimeMillis() + ACCESS_TOKEN_DURATION);
    }

    public String renewAccessToken(String token) {
        return remakeToken(token, System.currentTimeMillis() + ACCESS_TOKEN_DURATION);
    }

    public String createRefreshToken(Authentication authentication) {
        long duration = System.currentTimeMillis() + REFRESH_TOKEN_DURATION;
        String refreshToken = makeToken(authentication, duration);
        String subject = getSubject(refreshToken);
        refreshTokenRepository.save(subject, refreshToken, REFRESH_TOKEN_DURATION);
        return refreshToken;
    }

    public String renewRefreshToken(String token) {
        long expiration = getExpiration(token);
        String renewRefreshToken = remakeToken(token, expiration);
        String subject = getSubject(renewRefreshToken);
        refreshTokenRepository.save(subject, renewRefreshToken, REFRESH_TOKEN_DURATION);

        return renewRefreshToken;
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateExpirationOfToken(String token) {
        try {
            return !getClaims(token)
                .getExpiration()
                .before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public JwtAuthenticationResult getJwtAuthenticationResult(String token) {
        Claims claims = getClaims(token);
        String uid = claims.getSubject();
        String provider = claims.get(PROVIDER, String.class);
        String email = claims.get(EMAIL, String.class);
        List<? extends GrantedAuthority> grantedAuthorities =
            (List<SimpleGrantedAuthority>) claims.get(AUTHORITIES, List.class).stream()
                .map(authority-> new SimpleGrantedAuthority((String) authority))
                .collect(Collectors.toList());

        return new JwtAuthenticationResult(uid, provider, email, grantedAuthorities);
    }

    private String makeToken(Authentication authentication, long duration) {

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoUtil.getOAuth2UserInfo(
            (OAuth2AuthenticationToken) authentication);
        String id = oAuth2UserInfo.getId();
        String provider = oAuth2UserInfo.getProvider().toString();
        String email = oAuth2UserInfo.getEmail();
        List<String> authorities = authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority).
            collect(Collectors.toList());

        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setSubject(id)
            .claim(PROVIDER, provider)
            .claim(EMAIL, email)
            .claim(AUTHORITIES, authorities)
            .setIssuedAt(new Date())
            .setExpiration(new Date(duration))
            .signWith(SECRET_KEY)
            .compact();
    }

    private String remakeToken(String token, long duration) {
        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setClaims(getClaims(token))
            .setIssuedAt(new Date())
            .setExpiration(new Date(duration))
            .signWith(SECRET_KEY)
            .compact();
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (JwtException e) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        }
    }

    private long getExpiration(String token) {
        return getClaims(token).getExpiration().getTime();
    }
}
