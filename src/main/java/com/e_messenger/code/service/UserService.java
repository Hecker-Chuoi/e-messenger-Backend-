package com.e_messenger.code.service;

import com.e_messenger.code.dto.requests.PasswordChangeRequest;
import com.e_messenger.code.dto.requests.UserCreationRequest;
import com.e_messenger.code.dto.requests.UserUpdateRequest;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.mapstruct.UserMapper;
import com.e_messenger.code.repository.UserRepository;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepo;
    UserMapper userMapper;
    PasswordEncoder encoder;

// create
    public User signUp(UserCreationRequest request) {
        if(userRepo.findByPhoneNumber(request.getPhoneNumber()).isPresent())
            throw new AppException(StatusCode.PHONE_USED);

        User user = userMapper.toEntity(request);
        user.setPassword(encoder.encode(request.getPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        return userRepo.save(user);
    }

// read
    public User getUserByIdentifier(String identifier) {
        Optional<User> userByPhone = userRepo.findByPhoneNumber(identifier);
        if (userByPhone.isPresent()) {
            return userByPhone.get();
        }
        throw new AppException(StatusCode.USER_NOT_FOUND);
    }

    public User getMyInfo(){
        return getCurrentUser();
    }

    private User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String identifier = auth.getName();
        return getUserByIdentifier(identifier);
    }

// update
    public User updateInfo(UserUpdateRequest request){
        User user = getCurrentUser();
        userMapper.updateUser(user, request);
        user.setUpdatedAt(LocalDateTime.now());

        return userRepo.save(user);
    }

    public String changePassword(PasswordChangeRequest request){
        User user = getCurrentUser();
        if(!encoder.matches(request.getOldPassword(), user.getPassword()))
            throw new AppException(StatusCode.UNAUTHENTICATED);
        if(!request.getNewPassword().equals(request.getConfirmedPassword()))
            throw new AppException(StatusCode.UNAUTHENTICATED);

        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepo.save(user);
        return "Password changed successfully!";
    }
}
