package com.e_messenger.code.utils;

import com.e_messenger.code.entity.User;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserUtil {
    UserService service;

    public List<User> getValidUsers(List<String> userIds){
        LinkedHashSet<User> result = new LinkedHashSet<>();
        for(String userId : userIds){
            result.add(service.getUserById(userId));
        }

        if(result.size() != userIds.size()) {
            throw new AppException(StatusCode.UNCATEGORIZED);
        }
        return result.stream().toList();
    }
}
