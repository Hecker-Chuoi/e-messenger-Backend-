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
@TypeAlias("conversationCreation")
public class ConversationCreation extends ConversationNotification {
    String name;

    public ConversationCreation(GeneralType type, String actorId, String actorName, String conversationId, LocalDateTime time, String name) {
        super("Conversation created", actorId, actorName, conversationId, time, DetailConvNotiType.CREATE);
        this.name = name;
    }
}
