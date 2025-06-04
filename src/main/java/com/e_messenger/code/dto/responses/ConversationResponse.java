package com.e_messenger.code.dto.responses;

import com.e_messenger.code.entity.Participant;
import com.e_messenger.code.entity.enums.ConversationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ConversationResponse {
    String id;

    ConversationType type;
    String avatarUrl;
    String conversationName;
    List<Participant> participants;

    //fields for ui
    String lastMessage;
    String lastActorId;
    String lastActorName;
    Instant lastMessageTime;
}
