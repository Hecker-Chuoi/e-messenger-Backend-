package com.e_messenger.code.mapstruct;

import com.e_messenger.code.dto.requests.GroupCreationRequest;
import com.e_messenger.code.dto.requests.GroupUpdateRequest;
import com.e_messenger.code.dto.responses.ConversationResponse;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.message.Message;
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

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "lastMessage", source = "content")
    @Mapping(target = "lastSenderId", source = "actorId")
    @Mapping(target = "lastMessageTime", source = "time")
    @Mapping(target = "lastSenderName", source = "actorName")
    void updateLastSentInfo(@MappingTarget Conversation conversation, Message lastMessage);
}
