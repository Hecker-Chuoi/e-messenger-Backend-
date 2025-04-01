package com.e_messenger.code.service;

import com.e_messenger.code.dto.requests.UserCreationRequest;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.mapstruct.UserMapper;
import com.e_messenger.code.repository.UserRepository;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
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

    public User signUp(UserCreationRequest request) {
        if(userRepo.findByUsername(request.getUsername()).isPresent())
            throw new AppException(StatusCode.USERNAME_USED);
        if(userRepo.findByPhoneNumber(request.getUsername()).isPresent())
            throw new AppException(StatusCode.PHONE_USED);

        User user = userMapper.toEntity(request);
        user.setPassword(encoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepo.save(user);
    }

    public User getUserById(String id) {
        try{
            return userRepo.findById(new ObjectId(id)).orElseThrow(
                    () -> new AppException(StatusCode.USER_NOT_FOUND)
            );
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public User getUserByIdentifier(String identifier) {
        Optional<User> userByUsername = userRepo.findByUsername(identifier);
        if (userByUsername.isPresent()) {
            return userByUsername.get();
        }

        Optional<User> userByPhone = userRepo.findByPhoneNumber(identifier);
        if (userByPhone.isPresent()) {
            return userByPhone.get();
        }

        throw new AppException(StatusCode.USER_NOT_FOUND);
    }

}
