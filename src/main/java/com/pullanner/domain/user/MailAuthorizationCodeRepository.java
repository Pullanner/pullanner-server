package com.pullanner.domain.user;


import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MailAuthorizationCodeRepository {

    private static final int AUTHORIZATION_CODE_TIMEOUT = 185;
    private final RedisTemplate<String, String> redisTemplate;

    public void setCodeByEmail(String email, Integer code) {
        redisTemplate.opsForValue().set(email, String.valueOf(code), AUTHORIZATION_CODE_TIMEOUT, TimeUnit.SECONDS);
    }

    public boolean validateCode(String email, Integer code) {
        return String.valueOf(code).equals(redisTemplate.opsForValue().get(email));
    }
}
