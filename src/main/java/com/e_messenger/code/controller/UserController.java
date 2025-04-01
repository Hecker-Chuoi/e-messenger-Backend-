package com.e_messenger.code.controller;

import com.e_messenger.code.dto.requests.UserCreationRequest;
import com.e_messenger.code.dto.responses.ApiResponse;
import com.e_messenger.code.dto.responses.UserResponse;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.mapstruct.UserMapper;
import com.e_messenger.code.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService service;
    UserMapper mapper;

    @PostMapping("/sign-up")
    public ApiResponse<UserResponse> signUp(UserCreationRequest request){
        User result = service.signUp(request);
        return ApiResponse.<UserResponse>builder()
                .result(mapper.toResponse(result))
                .build();
    }

    @GetMapping("/{identifier}")
    public ApiResponse<UserResponse> getUser(@PathVariable String identifier){
        User result = service.getUserByIdentifier(identifier);
        return ApiResponse.<UserResponse>builder()
                .result(mapper.toResponse(result))
                .build();
    }
}
