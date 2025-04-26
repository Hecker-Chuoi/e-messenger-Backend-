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
    GroupChatServiceImpl groupChatService;
    ConversationMapper conversationMapper;

    @PostMapping
    public ApiResponse<ConversationResponse> createGroupChat(@RequestBody GroupCreationRequest request){
        Conversation result = groupChatService.createGroupChat(request);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @PutMapping("/{groupId}")
    public ApiResponse<ConversationResponse> updateGroupInfo(@PathVariable String groupId, @RequestBody GroupUpdateRequest request){
        Conversation result = groupChatService.updateGroupInfo(groupId, request);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @PutMapping("/{groupId}/participants/add")
    public ApiResponse<ConversationResponse> addParticipants(@PathVariable String groupId, @RequestBody List<String> participantIds){
        Conversation result = groupChatService.addParticipants(groupId, participantIds);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @PutMapping("/{groupId}/participants/remove")
    public ApiResponse<ConversationResponse> removeParticipants(@PathVariable String groupId, @RequestBody List<String> participantIds){
        Conversation result = groupChatService.removeParticipants(groupId, participantIds);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @DeleteMapping("/{groupId}/leave")
    public ApiResponse<String> leaveConversation(@PathVariable String groupId){
        boolean result = groupChatService.leaveConversation(groupId);
        return ApiResponse.<String>builder()
                .result("Leave group successfully!")
                .build();
    }

    @DeleteMapping("/{groupId}")
    public ApiResponse deleteGroup(@PathVariable String groupId){
        groupChatService.deleteGroup(groupId);
        return ApiResponse.builder()
                .result("Group deleted successfully!")
                .build();
    }

    @PutMapping("/{groupId}/set-owner")
    public ApiResponse<ConversationResponse> setOwner(@PathVariable String groupId, @RequestParam String ownerId){
        Conversation result = groupChatService.setOwner(groupId, ownerId);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @PutMapping("/{groupId}/set-coOwner")
    public ApiResponse<ConversationResponse> setCoOwner(@PathVariable String groupId, @RequestBody List<String> coOwnerIds){
        Conversation result = groupChatService.setCoOwner(groupId, coOwnerIds);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @PutMapping("/{groupId}/set-member")
    public ApiResponse<ConversationResponse> toMember(@PathVariable String groupId, @RequestBody List<String> participantIds){
        Conversation result = groupChatService.toMember(groupId, participantIds);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }
}