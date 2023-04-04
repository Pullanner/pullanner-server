package com.pullanner.redis;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void save(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    public String findByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteByKey(String key) {
        redisTemplate.delete(key);
    }
}
