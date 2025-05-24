package com.e_messenger.code.entity.message.conversation.update;

import com.e_messenger.code.entity.enums.DetailConvNotiType;
import com.e_messenger.code.entity.message.ConversationNotification;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;

import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@TypeAlias("changeAvatar")
public class ChangeAvatar extends ConversationNotification {
    public ChangeAvatar(String actorId, String actorName, String conversationId, LocalDateTime time) {
        super("Conversation's avatar has been changed", actorId, actorName, conversationId, time, DetailConvNotiType.CHANGE_AVATAR);
    }
}
