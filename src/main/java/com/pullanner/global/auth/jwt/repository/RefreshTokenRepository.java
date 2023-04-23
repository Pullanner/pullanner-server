package com.pullanner.global.auth.jwt.repository;

import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshTokenById(String refreshTokenId, String refreshToken, long duration) {
        redisTemplate.opsForValue().set(refreshTokenId, refreshToken, duration, TimeUnit.MILLISECONDS);
    }

    public String findRefreshTokenById(String refreshTokenId) {
        return redisTemplate.opsForValue().get(refreshTokenId);
    }

    public void deleteRefreshTokenById(String refreshTokenId) {
        redisTemplate.delete(refreshTokenId);
    }

    public void addRefreshTokenIdByUserId(String userId, String refreshTokenId) {
        redisTemplate.opsForList().rightPush(userId, refreshTokenId);
    }

    public List<String> findAllRefreshTokenIdsByUserId(String userId) {
        return redisTemplate.opsForList().range(userId, 0, -1);
    }
}
