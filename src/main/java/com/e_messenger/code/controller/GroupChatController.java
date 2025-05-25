package com.e_messenger.code.controller;

import com.e_messenger.code.dto.requests.conv.GroupCreationRequest;
import com.e_messenger.code.dto.requests.conv.GroupUpdateRequest;
import com.e_messenger.code.dto.responses.ApiResponse;
import com.e_messenger.code.dto.responses.ConversationResponse;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.mapstruct.ConversationMapper;
import com.e_messenger.code.service.impl.GroupChatServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

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

    @MessageMapping("/group/create")
    public ApiResponse<ConversationResponse> createGroupChat(@Payload GroupCreationRequest request, Principal principal){
        Conversation result = groupChatService.createGroupChat(request, principal);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @MessageMapping("/group/{groupId}/update")
    public ApiResponse<ConversationResponse> updateGroupInfo(@DestinationVariable String groupId, @Payload String newName, Principal principal){
        Conversation result = groupChatService.changeName(groupId, newName, principal);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @MessageMapping("/group/{groupId}/participants/add")
    public ApiResponse<ConversationResponse> addParticipants(@DestinationVariable String groupId, @Payload List<String> participantIds, Principal principal){
        Conversation result = groupChatService.addParticipants(groupId, participantIds, principal);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @MessageMapping("/group/{groupId}/participants/remove")
    public ApiResponse<ConversationResponse> removeParticipants(@DestinationVariable String groupId, @Payload List<String> participantIds, Principal principal){
        Conversation result = groupChatService.removeParticipants(groupId, participantIds, principal);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @MessageMapping("/group/{groupId}/leave")
    public ApiResponse<String> leaveConversation(@DestinationVariable String groupId, Principal principal){
        Conversation result = groupChatService.leaveConversation(groupId, principal);
        return ApiResponse.<String>builder()
                .result("Leave group successfully!")
                .build();
    }

    @MessageMapping("/group/{groupId}/delete")
    public ApiResponse deleteGroup(@DestinationVariable String groupId, Principal principal){
        groupChatService.deleteGroup(groupId, principal);
        return ApiResponse.builder()
                .result("Group deleted successfully!")
                .build();
    }

    @MessageMapping("/group/{groupId}/participants/set-owner/{newOwnerId}")
    public ApiResponse<ConversationResponse> setOwner(@DestinationVariable String groupId, @DestinationVariable String newOwnerId, Principal principal){
        Conversation result = groupChatService.setOwner(groupId, newOwnerId, principal);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @MessageMapping("/group/{groupId}/participants/set-co-owner")
    public ApiResponse<ConversationResponse> setCoOwner(@DestinationVariable String groupId, @Payload List<String> coOwnerIds, Principal principal){
        Conversation result = groupChatService.setCoOwner(groupId, coOwnerIds, principal);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }

    @MessageMapping("/group/{groupId}/participants/set-member")
    public ApiResponse<ConversationResponse> toMember(@DestinationVariable String groupId, @Payload List<String> participantIds, Principal principal){
        Conversation result = groupChatService.toMember(groupId, participantIds, principal);
        return ApiResponse.<ConversationResponse>builder()
                .result(conversationMapper.toResponse(result))
                .build();
    }
}