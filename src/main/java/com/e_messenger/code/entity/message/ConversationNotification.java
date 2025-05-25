package com.e_messenger.code.entity.message;

import com.e_messenger.code.entity.enums.DetailActionType;
import com.e_messenger.code.entity.enums.GeneralType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public abstract class ConversationNotification extends Message {
    @Builder.ObtainVia(method = "getActionType", isStatic = false)
    DetailActionType actionType;

    @Override
    public GeneralType getType() {
        return GeneralType.CONVERSATION;
    }

    public abstract DetailActionType getActionType();
}
