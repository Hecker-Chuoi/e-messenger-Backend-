package com.e_messenger.code.dto.responses;

import com.e_messenger.code.entity.Conversation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ConversationResponse {
    String id;

    Conversation.ConversationType type;
    String conversationName;
    List<String> participantIds;

    //fields for ui
    String lastMessage;
    String lastSenderId;
    String lastSenderName;
    LocalDateTime lastMessageTime;
}
