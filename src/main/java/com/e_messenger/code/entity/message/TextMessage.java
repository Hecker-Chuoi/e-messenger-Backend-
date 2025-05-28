package com.e_messenger.code.entity.message;

import com.e_messenger.code.entity.enums.GeneralType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("textMessage")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TextMessage extends Message{

    @Override
    public GeneralType getType() {
        return GeneralType.TEXT;
    }
}
