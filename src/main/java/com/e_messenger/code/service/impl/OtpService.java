package com.e_messenger.code.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
//@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpService {
    SecureRandom secureRandom = new SecureRandom();
    RedisTemplate<String, String> redisTemplate;

    @Value("${spring.cache.redis.otp-ttl}")
    @NonFinal
    int otpTtl;

//    @Bean
    public String generateNewKey(){
        String newKey = generateOtp();
        redisTemplate.opsForValue().set(
                getOtpKey("Test"),
                newKey,
                otpTtl,
                TimeUnit.SECONDS
        );
        return newKey;
    }

    public boolean isKeyValid(String id, String key){
        return Objects.equals(getCachedOtp(id), key);
    }

    public void deleteKey(String id){
        redisTemplate.delete(getOtpKey(id));
    }

    private String getCachedOtp(String id){
        return redisTemplate.opsForValue().get(getOtpKey(id));
    }

    private String getOtpKey(String id){
        return "otp:%s".formatted(id);
    }

    private String generateOtp(){
        return String.valueOf(100000 + secureRandom.nextInt(900000));
    }
}
