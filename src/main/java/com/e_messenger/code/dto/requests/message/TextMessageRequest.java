package com.e_messenger.code.dto.requests.message;

import com.e_messenger.code.entity.enums.GeneralType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TextMessageRequest extends MessageRequest {
    String text;

    @Setter(AccessLevel.NONE)
    @Builder.Default
    final GeneralType type = GeneralType.TEXT;
}
