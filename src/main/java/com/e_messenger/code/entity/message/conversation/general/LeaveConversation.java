package com.e_messenger.code.entity.message.conversation.general;

import com.e_messenger.code.entity.enums.DetailActionType;
import com.e_messenger.code.entity.message.ConversationNotification;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@TypeAlias("leaveConversation")
public class LeaveConversation extends ConversationNotification {

    @Builder.Default
    String content = "Someone has left the conversation";

    @Override
    public DetailActionType getActionType() {
        return DetailActionType.LEAVE;
    }
}
