package com.e_messenger.code.entity.message.conversation.update;

import com.e_messenger.code.entity.enums.DetailConvNotiType;
import com.e_messenger.code.entity.enums.GeneralType;
import com.e_messenger.code.entity.message.ConversationNotification;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;

import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@TypeAlias("changeName")
public class ChangeName extends ConversationNotification {
    String oldName;
    String newName;

    public ChangeName(String actorId, String actorName, String conversationId, LocalDateTime time, String oldName, String newName) {
        super("Conversation's name changed: from %s to %s".formatted(oldName, newName), actorId, actorName, conversationId, time, DetailConvNotiType.CHANGE_NAME);
        this.oldName = oldName;
        this.newName = newName;
    }
}