package com.e_messenger.code.dto.requests.message;

import com.e_messenger.code.entity.enums.MediaType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MediaMessageRequest {
    MediaType mediaType;
    String uploadedUrl;
}
