package com.pullanner.auth.service;

import com.pullanner.redis.RedisRepository;
import com.pullanner.auth.jwt.AccessTokenProvider;
import com.pullanner.auth.exception.InvalidTokenException;
import com.pullanner.auth.jwt.RefreshToken;
import com.pullanner.auth.jwt.RefreshTokenProvider;
import com.pullanner.auth.jwt.Token;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private static final long REFRESH_TOKEN_DURATION_MINUTE = 10L;

    private final RedisRepository redisRepository;
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    public Token createAccessToken(Authentication authentication) {
        return accessTokenProvider.createToken(authentication);
    }

    public Token createRefreshToken(Authentication authentication) {
        Token refreshToken = refreshTokenProvider.createToken(authentication);
        redisRepository.save(String.valueOf(authentication.getPrincipal()), refreshToken.getToken(), Duration.ofMinutes(REFRESH_TOKEN_DURATION_MINUTE));
        return refreshToken;
    }

    public String validateDurationOfRefreshToken(RefreshToken refreshToken) {
        String memberId = refreshToken.getUserInfo();

        if (redisRepository.findByKey(memberId) == null) {
            throw new InvalidTokenException("refresh 토큰의 만료기한이 지났습니다. 다시 로그인 해주세요.");
        }

        return memberId;
    }
}
