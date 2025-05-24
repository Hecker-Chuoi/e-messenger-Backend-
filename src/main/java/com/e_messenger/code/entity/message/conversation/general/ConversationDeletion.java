package com.e_messenger.code.entity.message.conversation.general;

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
@TypeAlias("conversationDeletion")
public class ConversationDeletion extends ConversationNotification {
    String name;

    public ConversationDeletion(String actorId, String actorName, String conversationId, LocalDateTime time, String name) {
        super("[Conversation {%s}'s been deleted]".formatted(name), actorId, actorName, conversationId, time, DetailConvNotiType.DELETE);
        this.name = name;
    }
}
