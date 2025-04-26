package com.e_messenger.code.controller;

import com.e_messenger.code.dto.responses.ApiResponse;
import com.e_messenger.code.dto.responses.ConversationResponse;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.mapstruct.ConversationMapper;
import com.e_messenger.code.service.impl.DirectChatServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/direct")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "user token")
public class DirectChatController {
    DirectChatServiceImpl service;
    ConversationMapper conversationMapper;

    @PostMapping("/{otherId}")
    public ApiResponse<ConversationResponse> createDirectChat(@PathVariable String otherId){
        Conversation result = service.createDirectChat(otherId);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @DeleteMapping("/{conversationId}")
    public ApiResponse<String> deleteConversation(@PathVariable String conversationId){
        boolean result = service.leaveConversation(conversationId);
        return ApiResponse.<String>builder()
                .result("Deleted successfully!")
                .build();
    }
}
