package com.e_messenger.code.dto.responses;

import com.e_messenger.code.entity.enums.GeneralType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class MessageResponse {
    String content;
    String actorId;
    String actorName;
    String conversationId;
    LocalDateTime time;
    GeneralType type;
}
