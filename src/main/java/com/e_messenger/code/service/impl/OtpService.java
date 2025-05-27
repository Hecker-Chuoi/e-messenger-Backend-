package com.e_messenger.code.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpService {
    SecureRandom secureRandom = new SecureRandom();
    RedisTemplate<String, String> redisTemplate;

    @Value("${spring.cache.redis.otp-ttl}")
    @NonFinal
    int otpTtl;

    public String generateNewKey(String id){
        String newKey = generateKey();
        redisTemplate.opsForValue().set(
                getCacheKey(id),
                newKey,
                otpTtl,
                TimeUnit.SECONDS
        );
        return newKey;
    }

    public boolean isKeyValid(String id, String key){
        return Objects.equals(getCacheKey(id), key);
    }

    public void deleteKey(String id){
        redisTemplate.delete(getCacheKey(id));
    }

    private String getCacheKey(String id){
        return "otp:%s".formatted(id);
    }

    private String generateKey(){
        return String.valueOf(100000 + secureRandom.nextInt(900000));
    }
}
