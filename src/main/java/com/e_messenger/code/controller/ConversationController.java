package com.e_messenger.code.controller;

import com.e_messenger.code.dto.responses.ApiResponse;
import com.e_messenger.code.dto.responses.ConversationResponse;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.mapstruct.ConversationMapper;
import com.e_messenger.code.service.ConversationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/conversation")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "user token")
public class ConversationController {
    ConversationService service;
    ConversationMapper mapper;

    @GetMapping("/directs/{otherIdentifier}")
    public ApiResponse<ConversationResponse> getDirectChat(@PathVariable String otherIdentifier){
        Conversation result = service.getDirectChat(otherIdentifier);
        return ApiResponse.<ConversationResponse>builder()
                .result(mapper.toResponse(result))
                .build();
    }

    @GetMapping("/directs")
    public ApiResponse<List<ConversationResponse>> getAllDirectChat(){
        List<Conversation> result = service.getAllDirectChat();
        return ApiResponse.<List<ConversationResponse>>builder()
                .result(mapper.toResponses(result))
                .build();
    }

    @GetMapping("/groups")
    public ApiResponse<List<ConversationResponse>> getAllGroupChat(){
        List<Conversation> result = service.getAllGroupChat();
        return ApiResponse.<List<ConversationResponse>>builder()
                .result(mapper.toResponses(result))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<ConversationResponse>> getAllConversation(){
        List<Conversation> result = service.getAllConversation();
        return ApiResponse.<List<ConversationResponse>>builder()
                .result(mapper.toResponses(result))
                .build();
    }
}
