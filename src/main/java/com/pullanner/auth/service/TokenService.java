package com.pullanner.auth.service;

import com.pullanner.redis.RedisRepository;
import com.pullanner.auth.jwt.AccessTokenProvider;
import com.pullanner.auth.exception.InvalidTokenException;
import com.pullanner.auth.jwt.RefreshToken;
import com.pullanner.auth.jwt.RefreshTokenProvider;
import com.pullanner.auth.jwt.Token;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final RedisRepository redisRepository;
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    public Token createAccessToken(String email) {
        return accessTokenProvider.createToken(email);
    }

    public Token createRefreshToken(String email) {
        Token refreshToken = refreshTokenProvider.createToken(email);
        redisRepository.save(email, refreshToken.getToken(), Duration.ofMinutes(refreshToken.getDuration()));
        return refreshToken;
    }

    public void validateRefreshToken(RefreshToken refreshToken) {
        if (redisRepository.findByKey(refreshToken.getUserEmail()) == null) {
            throw new InvalidTokenException("해당되는 Refresh 토큰이 존재하지 않습니다.");
        }
    }

    public void renewRefreshToken(Token refreshToken) {
        redisRepository.save(refreshToken.getUserEmail(), refreshToken.getToken(), Duration.ofMinutes(refreshToken.getDuration()));
    }
}
