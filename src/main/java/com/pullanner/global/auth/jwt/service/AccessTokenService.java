package com.pullanner.global.auth.jwt.service;

import com.pullanner.global.auth.jwt.dto.JwtAuthenticationResult;
import com.pullanner.global.auth.oauth2.dto.OAuth2UserInfo;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenService extends TokenService {

    private static final long ACCESS_TOKEN_DURATION = 2 * 60_000; // 2 minutes

    public String createAccessToken(OAuth2UserInfo oAuth2UserInfo, List<String> authorities) {
        return makeToken(oAuth2UserInfo, authorities, System.currentTimeMillis() + ACCESS_TOKEN_DURATION);
    }

    public String renewAccessToken(String token) {
        return remakeToken(token, System.currentTimeMillis() + ACCESS_TOKEN_DURATION);
    }

    public String validateAndGetSubject(String token) {
        return getClaims(token).getSubject();
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
}
