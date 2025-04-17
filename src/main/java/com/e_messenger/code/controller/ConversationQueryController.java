package com.e_messenger.code.controller;

import com.e_messenger.code.dto.responses.ApiResponse;
import com.e_messenger.code.dto.responses.ConversationResponse;
import com.e_messenger.code.dto.responses.UserResponse;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.mapstruct.ConversationMapper;
import com.e_messenger.code.mapstruct.UserMapper;
import com.e_messenger.code.service.ConversationQueryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "user token")
public class ConversationQueryController {
    ConversationQueryService mainService;
    ConversationMapper conversationMapper;
    UserMapper userMapper;

    @GetMapping("/direct/{otherIdentifier}")
    public ApiResponse<ConversationResponse> getDirectChat(@PathVariable String otherIdentifier){
        Conversation result = mainService.getDirectChat(otherIdentifier);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @GetMapping("/{conversationId}")
    public ApiResponse<ConversationResponse> getConversation(@PathVariable String conversationId){
        Conversation result = mainService.getConversationById(conversationId);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }
    
    @GetMapping("/{groupId}/participants")
    public ApiResponse<List<UserResponse>> getParticipants(@PathVariable String groupId){
        List<User> result = mainService.getParticipants(groupId);
        return ApiResponse.<List<UserResponse>>builder()
                .result(userMapper.toResponses(result))
                .build();
    }

    @GetMapping("/direct")
    public ApiResponse<List<ConversationResponse>> getAllDirectChat(
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize){
        List<Conversation> result = mainService.getAllDirectChat(pageNum, pageSize);
        return ApiResponse.<List<ConversationResponse>>builder()
                .result(conversationMapper.toResponses(result))
                .build();
    }

    @GetMapping("/group")
    public ApiResponse<List<ConversationResponse>> getAllGroupChat(
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        List<Conversation> result = mainService.getAllGroupChat(pageNum, pageSize);
        return ApiResponse.<List<ConversationResponse>>builder()
                .result(conversationMapper.toResponses(result))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<ConversationResponse>> getAllConversation(
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        List<Conversation> result = mainService.getAllConversation(pageNum, pageSize);
        return ApiResponse.<List<ConversationResponse>>builder()
                .result(conversationMapper.toResponses(result))
                .build();
    }
}
