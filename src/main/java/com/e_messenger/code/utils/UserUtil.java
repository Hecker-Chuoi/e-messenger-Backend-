package com.e_messenger.code.utils;

import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserUtil {
    UserService service;

    @Bean
    public List<String> findNotValidUserId(List<String> userIds){
        List<String> result = new ArrayList<>();
        for(String userId : userIds){
            try{
                service.getUserById(userId);
            }
            catch(AppException e){
                result.add(userId);
            }
        }
        return result;
    }
}
