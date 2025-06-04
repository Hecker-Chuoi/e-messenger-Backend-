package com.e_messenger.code.entity;

import com.e_messenger.code.entity.enums.ConversationRole;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Participant {
    String participantId;
    String phoneNumber;
    ConversationRole role;
    Instant joinAt;
    @Transient
    String displayName;
    @Transient
    String avatarUrl;
    @Transient
    String fcmToken;

    @PersistenceCreator
    public Participant(String participantId, String phoneNumber, ConversationRole role, Instant joinAt) {
        this.participantId = participantId;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.joinAt = joinAt;
    }
}
