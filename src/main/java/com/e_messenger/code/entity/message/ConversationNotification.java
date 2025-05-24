package com.e_messenger.code.entity.message;

import com.e_messenger.code.entity.enums.DetailConvNotiType;
import com.e_messenger.code.entity.enums.GeneralType;

import java.time.LocalDateTime;

public abstract class ConversationNotification extends Message {
    DetailConvNotiType detailType;

    public ConversationNotification(String content, String actorId, String actorName, String conversationId, LocalDateTime time, DetailConvNotiType detailType) {
        super(content, GeneralType.CONVERSATION, actorId, actorName, conversationId, time);
        this.detailType = detailType;
    }
}
