package com.e_messenger.code.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "conversations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Conversation {
    @Id
    @Indexed
    String id;

    public enum ConversationType{
        DIRECT,
        GROUP
    }

    ConversationType type;
    String conversationName;
    List<String> participantIds;

    //fields for ui
    String lastMessage;
    String lastSenderId;
    String lastSenderName;
    LocalDateTime lastMessageTime;
}