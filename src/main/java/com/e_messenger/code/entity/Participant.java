package com.e_messenger.code.entity;

import com.e_messenger.code.entity.enums.ConversationRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Participant {
    String participantId;
    String displayName;
    String phoneNumber;
    ConversationRole role;
    LocalDateTime joinAt;
}
