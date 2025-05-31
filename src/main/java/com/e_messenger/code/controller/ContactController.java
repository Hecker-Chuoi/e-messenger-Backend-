package com.e_messenger.code.controller;

import com.e_messenger.code.dto.responses.ApiResponse;
import com.e_messenger.code.dto.responses.UserResponse;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.mapstruct.UserMapper;
import com.e_messenger.code.service.impl.ContactService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContactController {
    ContactService contactService;
    UserMapper userMapper;

    @GetMapping
    public ApiResponse<List<UserResponse>> getRecentContact(
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            Principal principal) {
        List<User> result = contactService.getDirectContacts(principal.getName(), pageSize, pageNum);
        return ApiResponse.<List<UserResponse>>builder()
                .result(userMapper.toResponses(result))
                .build();
    }
}
