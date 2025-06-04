package com.e_messenger.code.controller;

import com.e_messenger.code.dto.responses.ApiResponse;
import com.e_messenger.code.dto.responses.ConversationResponse;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.mapstruct.ConversationMapper;
import com.e_messenger.code.service.ConversationQueryService;
import com.e_messenger.code.service.DirectChatService;
import com.e_messenger.code.service.impl.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/direct")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "user token")
public class DirectChatController {
    DirectChatService service;
    ConversationMapper conversationMapper;

    @PostMapping("/{otherId}")
    public ApiResponse<ConversationResponse> createDirectChat(@PathVariable String otherId, Principal principal) {
        Conversation result = service.createDirectChat(otherId, principal);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @DeleteMapping("/{conversationId}")
    public ApiResponse<String> deleteConversation(@PathVariable String conversationId, Principal principal) {
        service.deleteConversation(conversationId, principal);
        return ApiResponse.<String>builder()
                .result("Successfully")
                .build();
    }
}