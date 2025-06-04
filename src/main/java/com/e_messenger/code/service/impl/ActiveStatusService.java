package com.e_messenger.code.service.impl;

import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActiveStatusService {
    RedisTemplate<String, String> redisTemplate;
    ObjectMapper objectMapper;

    @Value("${web-socket.active-status.ttl}")
    @NonFinal
    int activeStatusTtl;

    public record ActiveStatus(Boolean isActive, Instant lastActiveTime){}

    public ActiveStatus getActiveStatus(String userId) {
        String json = redisTemplate.opsForValue().get(getCachedStatusKey(userId));
        try{
            if(json == null)
                return new ActiveStatus(false, null);
            return objectMapper.readValue(json, ActiveStatus.class);
        } catch (JsonProcessingException e) {
            throw new AppException(StatusCode.UNCATEGORIZED);
        }
    }

    public void setActiveStatus(String userId, boolean isActive){
        try{
            String value = objectMapper.writeValueAsString(new ActiveStatus(isActive, Instant.now()));
            redisTemplate.opsForValue().set(
                    getCachedStatusKey(userId),
                    value,
                    activeStatusTtl,
                    TimeUnit.HOURS
            );
        }
        catch (JsonProcessingException e){
            throw new AppException(StatusCode.UNCATEGORIZED);
        }
    }

    private String getCachedStatusKey(String userId){
        return "status:%s".formatted(userId);
    }
}
