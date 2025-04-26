package com.e_messenger.code.entity;

import com.e_messenger.code.entity.enums.ConversationType;
import lombok.*;
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
    @Indexed(unique = true)
    String id;

    ConversationType type;
    String conversationName;
    @Indexed
    List<Participant> participants;

    //fields for ui
    String lastMessage;
    String lastSenderId;
    String lastSenderName;
    LocalDateTime lastMessageTime;
}