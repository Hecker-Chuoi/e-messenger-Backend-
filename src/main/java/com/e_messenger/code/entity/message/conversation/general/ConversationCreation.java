package com.e_messenger.code.entity.message.conversation.general;

import com.e_messenger.code.entity.enums.DetailActionType;
import com.e_messenger.code.entity.message.ConversationNotification;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@TypeAlias("conversationCreation")
public class ConversationCreation extends ConversationNotification {
    String name;

    @Override
    public DetailActionType getActionType() {
        return DetailActionType.CREATE;
    }

}
