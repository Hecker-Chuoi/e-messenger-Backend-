package com.e_messenger.code.dto.requests.message;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TextMessageRequest {
    String text;
}
