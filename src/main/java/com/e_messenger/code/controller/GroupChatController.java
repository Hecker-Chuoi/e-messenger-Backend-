package com.e_messenger.code.controller;

import com.e_messenger.code.dto.requests.GroupCreationRequest;
import com.e_messenger.code.dto.requests.GroupUpdateRequest;
import com.e_messenger.code.dto.responses.ApiResponse;
import com.e_messenger.code.dto.responses.ConversationResponse;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.mapstruct.ConversationMapper;
import com.e_messenger.code.service.impl.GroupChatServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "user token")
public class GroupChatController {
    GroupChatServiceImpl mainService;
    ConversationMapper mainMapper;

    @PostMapping
    public ApiResponse<ConversationResponse> createGroupChat(@RequestBody GroupCreationRequest request){
        Conversation result = mainService.createGroupChat(request);
        return ApiResponse.<ConversationResponse>builder()
                .result(mainMapper.toResponse(result))
                .build();
    }

    @PutMapping("/{groupId}")
    public ApiResponse<ConversationResponse> updateGroupInfo(@PathVariable String groupId, @RequestBody GroupUpdateRequest request){
        Conversation result = mainService.updateGroupInfo(groupId, request);
        return ApiResponse.<ConversationResponse>builder()
                .result(mainMapper.toResponse(result))
                .build();
    }

    @PutMapping("/{groupId}/participants/add")
    public ApiResponse<ConversationResponse> addParticipants(@PathVariable String groupId, @RequestBody List<String> participantIds){
        Conversation result = mainService.addParticipants(groupId, participantIds);
        return ApiResponse.<ConversationResponse>builder()
                .result(mainMapper.toResponse(result))
                .build();
    }

    @PutMapping("/{groupId}/participants/remove")
    public ApiResponse<ConversationResponse> removeParticipants(@PathVariable String groupId, @RequestBody List<String> participantIds){
        Conversation result = mainService.removeParticipants(groupId, participantIds);
        return ApiResponse.<ConversationResponse>builder()
                .result(mainMapper.toResponse(result))
                .build();
    }

    @DeleteMapping("/{groupId}/leave")
    public ApiResponse<String> leaveConversation(@PathVariable String groupId){
        boolean result = mainService.leaveConversation(groupId);
        return ApiResponse.<String>builder()
                .result("Leave group successfully!")
                .build();
    }
}