package com.e_messenger.code.entity.message;

import com.e_messenger.code.entity.enums.GeneralType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public abstract class Message {
    String content;
    GeneralType type;
    String actorId;
    String actorName;
    String conversationId;
    LocalDateTime time;
}
