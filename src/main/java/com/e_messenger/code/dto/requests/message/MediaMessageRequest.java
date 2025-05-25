package com.e_messenger.code.dto.requests.message;

import com.e_messenger.code.entity.enums.GeneralType;
import com.e_messenger.code.entity.enums.MediaType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MediaMessageRequest extends MessageRequest {
    MediaType mediaType;
    String uploadedUrl;

    @Setter(AccessLevel.NONE)
    @Builder.Default
    final GeneralType type = GeneralType.MEDIA;
}
