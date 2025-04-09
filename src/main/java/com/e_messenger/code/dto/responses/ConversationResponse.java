package com.e_messenger.code.dto.responses;

import com.e_messenger.code.entity.Conversation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversationResponse {
    String id;

    Conversation.ConversationType type;
    String name; //nullable, only required for group chat

    //fields for ui
    String lastMessage;
    String lastSenderId;
    String lastSenderName;
    LocalDateTime lastMessageTime;
}
