package com.e_messenger.code.controller;

import com.e_messenger.code.dto.requests.AuthRequest;
import com.e_messenger.code.dto.responses.ApiResponse;
import com.e_messenger.code.dto.responses.AuthResponse;
import com.e_messenger.code.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService service;

    @PostMapping("/log-in")
    public ApiResponse<AuthResponse> logIn(@RequestBody AuthRequest request){
        return ApiResponse.<AuthResponse>builder()
                .result(service.logIn(request))
                .build();
    }

    @GetMapping("/refresh-token")
    public ApiResponse<AuthResponse> getNewAccessToken(){
        return ApiResponse.<AuthResponse>builder()
                .result(service.refreshAccessToken())
                .build();
    }
}
