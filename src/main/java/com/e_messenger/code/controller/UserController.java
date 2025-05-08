package com.e_messenger.code.controller;

import com.e_messenger.code.dto.requests.PasswordChangeRequest;
import com.e_messenger.code.dto.requests.UserCreationRequest;
import com.e_messenger.code.dto.requests.UserUpdateRequest;
import com.e_messenger.code.dto.responses.ApiResponse;
import com.e_messenger.code.dto.responses.UserResponse;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.mapstruct.UserMapper;
import com.e_messenger.code.service.impl.CloudStorageService;
import com.e_messenger.code.service.impl.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService service;
    UserMapper mapper;
    CloudStorageService storageService;

//create
    @PostMapping
    public ApiResponse<UserResponse> signUp(@RequestParam(value = "avatar", required = false) MultipartFile avatar,
                                            @RequestBody @Valid UserCreationRequest request){
        User result = service.signUp(request);
        return ApiResponse.<UserResponse>builder()
                .result(mapper.toResponse(result))
                .build();
    }

//read
    @SecurityRequirement(name = "user token")
    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getMyInfo(){
        User result = service.getCurrentUser();
        return ApiResponse.<UserResponse>builder()
                .result(mapper.toResponse(result))
                .build();
    }

    @SecurityRequirement(name = "user token")
    @GetMapping("/{identifier}")
    public ApiResponse<UserResponse> findUser(@PathVariable String identifier){
        User result = service.getUserByIdentifier(identifier);
        return ApiResponse.<UserResponse>builder()
                .result(mapper.toResponse(result))
                .build();
    }

//update
    @SecurityRequirement(name = "user token")
    @PutMapping
    public ApiResponse<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest request){
        User user = service.updateInfo(request);
        return ApiResponse.<UserResponse>builder()
                .result(mapper.toResponse(user))
                .build();
    }

    @SecurityRequirement(name = "user token")
    @PutMapping("/avatars")
    public ApiResponse<UserResponse> updateAvatar(@RequestParam MultipartFile avatar) throws IOException {
        User user = service.setAvatar(avatar);
        return ApiResponse.<UserResponse>builder()
                .result(mapper.toResponse(user))
                .build();
    }

    @SecurityRequirement(name = "user token")
    @PutMapping("/passwords")
    public ApiResponse<String> changePassword(@RequestBody @Valid PasswordChangeRequest request){
        return ApiResponse.<String>builder()
                .result(service.changePassword(request))
                .build();
    }

//delete
}
