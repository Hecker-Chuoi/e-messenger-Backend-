package com.e_messenger.code.controller;

import com.e_messenger.code.dto.requests.conv.GroupCreationRequest;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
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
    public ApiResponse<ConversationResponse> createGroupChat(@RequestBody GroupCreationRequest request, Principal principal){
        Conversation result = groupChatService.createGroupChat(request, principal);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @PutMapping("/{groupId}")
    public ApiResponse<ConversationResponse> changeName(@PathVariable String groupId, @RequestBody String newName, Principal principal){
        Conversation result = groupChatService.changeName(groupId, newName, principal);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @PutMapping("/{groupId}/avatars")
    public ApiResponse<ConversationResponse> changeAvatar(
            @PathVariable String groupId,
            @RequestParam MultipartFile avatar,
            Principal principal
    ) throws IOException {
        Conversation conv = groupChatService.changeAvatar(groupId, avatar, principal);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(conv))
                .build();
    }

    @PutMapping("/{groupId}/participants/add")
    public ApiResponse<ConversationResponse> addParticipants(@PathVariable String groupId, @RequestBody List<String> participantIds, Principal principal){
        Conversation result = groupChatService.addParticipants(groupId, participantIds, principal);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @PutMapping("/{groupId}/participants/remove")
    public ApiResponse<ConversationResponse> removeParticipants(@PathVariable String groupId, @RequestBody List<String> participantIds, Principal principal){
        Conversation result = groupChatService.removeParticipants(groupId, participantIds, principal);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @PutMapping("/{groupId}/leave")
    public ApiResponse<String> leaveConversation(@PathVariable String groupId, Principal principal){
        Conversation result = groupChatService.leaveGroup(groupId, principal);
        return ApiResponse.<String>builder()
                .result("Leave group successfully!")
                .build();
    }

    @DeleteMapping("/{groupId}/delete")
    public ApiResponse deleteGroup(@PathVariable String groupId, Principal principal){
        groupChatService.deleteConversation(groupId, principal);
        return ApiResponse.builder()
                .result("Group deleted successfully!")
                .build();
    }

    @PutMapping("/{groupId}/set-owner")
    public ApiResponse<ConversationResponse> setOwner(@PathVariable String groupId, @RequestParam String ownerId, Principal principal){
        Conversation result = groupChatService.setOwner(groupId, ownerId, principal);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @PutMapping("/{groupId}/set-coOwner")
    public ApiResponse<ConversationResponse> setCoOwner(@PathVariable String groupId, @RequestBody List<String> coOwnerIds, Principal principal){
        Conversation result = groupChatService.setCoOwner(groupId, coOwnerIds, principal);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @PutMapping("/{groupId}/set-member")
    public ApiResponse<ConversationResponse> setMember(@PathVariable String groupId, @RequestBody List<String> participantIds, Principal principal){
        Conversation result = groupChatService.toMember(groupId, participantIds, principal);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }
}