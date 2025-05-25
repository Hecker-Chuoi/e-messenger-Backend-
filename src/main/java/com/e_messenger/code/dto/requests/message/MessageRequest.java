package com.e_messenger.code.dto.requests.message;

import com.e_messenger.code.entity.enums.GeneralType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public abstract class MessageRequest {
    GeneralType type;
}
