package com.e_messenger.code.entity.message;

import com.e_messenger.code.entity.enums.GeneralType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Document(collection = "messages")
@TypeAlias("message")
@Data
@SuperBuilder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public abstract class Message {
    String content;
    String actorId;
    String actorName;
    String actorAvatarUrl;
    String conversationId;
    Instant time;

    @Builder.ObtainVia(method = "getType", isStatic = false)
    final GeneralType type;

    public abstract GeneralType getType();
}