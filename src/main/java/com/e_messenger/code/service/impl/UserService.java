package com.e_messenger.code.service.impl;

import com.e_messenger.code.dto.requests.user.PasswordChangeRequest;
import com.e_messenger.code.dto.requests.user.UserCreationRequest;
import com.e_messenger.code.dto.requests.user.UserUpdateRequest;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.entity.enums.Gender;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.mapstruct.UserMapper;
import com.e_messenger.code.repository.UserRepository;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepo;
    UserMapper userMapper;
    PasswordEncoder encoder;
    CloudStorageService storageService;
    ActiveStatusService statusService;

    @NonFinal
    @Value("${cloud.avatar.otherDefault}")
    String otherDefault;

    @NonFinal
    @Value("${cloud.avatar.manDefault}")
    String manDefault;

    @NonFinal
    @Value("${cloud.avatar.womanDefault}")
    String womanDefault;

// create
    public User signUp(UserCreationRequest request) {
        if(userRepo.findByPhoneNumber(request.getPhoneNumber()).isPresent())
            throw new AppException(StatusCode.PHONE_USED);

        User user = userMapper.toEntity(request);
        user.setId(UUID.randomUUID().toString());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setUpdatedAt(Instant.now());

        try{
            if(user.getGender().equals(Gender.MALE))
                user.setAvatarUrl(manDefault);
            else if(user.getGender().equals(Gender.FEMALE))
                user.setAvatarUrl(womanDefault);
            else user.setAvatarUrl(otherDefault);
        } catch (Exception e) {
            user.setAvatarUrl(otherDefault);
        }

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

    public User getUserById(String id) {
        User user = userRepo.findById(id).orElseThrow(
                () -> new AppException(StatusCode.UNCATEGORIZED)
        );
        user.setActiveStatus(statusService.getActiveStatus(id));
        return user;
    }

    public User getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String identifier = auth.getName();
        return getUserById(identifier);
    }

// update
    public User updateInfo(UserUpdateRequest request){
        User user = getCurrentUser();
        userMapper.updateUser(user, request);
        user.setUpdatedAt(Instant.now());

        return userRepo.save(user);
    }

    public User setAvatar(MultipartFile file) throws IOException {
        User user = getCurrentUser();
        Map result = storageService.uploadFile(file);
        user.setAvatarUrl((String) result.get("url"));
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

    public User updateFcmToken(String newToken){
        User user = getCurrentUser();
        user.setFcmToken(newToken);

        userRepo.save(user);
        return user;
    }
}
