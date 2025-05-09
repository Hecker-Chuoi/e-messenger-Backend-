package com.e_messenger.code.mapstruct;

import com.e_messenger.code.dto.requests.GroupCreationRequest;
import com.e_messenger.code.dto.requests.GroupUpdateRequest;
import com.e_messenger.code.dto.responses.ConversationResponse;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    ConversationResponse toResponse(Conversation conversation);
    List<ConversationResponse> toResponses(List<Conversation> conversation);

    @Mapping(target = "conversationName", source = "groupName")
    Conversation toEntity(GroupCreationRequest request);

    @Mapping(target = "conversationName", source = "groupName")
    void update(@MappingTarget Conversation conversation, GroupUpdateRequest request);

    @Mapping(target = "lastMessage", source = "content")
    @Mapping(target = "lastSenderName", source = "senderName")
    @Mapping(target = "lastMessageTime", source = "sentAt")
    @Mapping(target = "lastSenderId", source = "senderId")
    @Mapping(target = "type", ignore = true)
    void updateLastSentInfo(@MappingTarget Conversation conversation, Message lastMessage);
}
