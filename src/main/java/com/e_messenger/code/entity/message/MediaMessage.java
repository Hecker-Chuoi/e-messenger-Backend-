package com.e_messenger.code.entity.message;

import com.e_messenger.code.entity.enums.GeneralType;
import com.e_messenger.code.entity.enums.MediaType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@TypeAlias("mediaMessage")
public class MediaMessage extends Message{
    MediaType mediaType;
    String url;

    @Builder.Default
    String content = "[Media message]";

    @Override
    public GeneralType getType() {
        return GeneralType.MEDIA;
    }
}
