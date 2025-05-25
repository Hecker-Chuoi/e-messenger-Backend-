package com.e_messenger.code.entity.message.conversation.update;

import com.e_messenger.code.entity.enums.DetailActionType;
import com.e_messenger.code.entity.message.ConversationNotification;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@TypeAlias("changeName")
public class ChangeName extends ConversationNotification {
    String oldName;
    String newName;

    @Builder.Default
    String content = "Conversation's name has changed";

    @Override
    public DetailActionType getActionType() {
        return DetailActionType.CHANGE_NAME;
    }

}