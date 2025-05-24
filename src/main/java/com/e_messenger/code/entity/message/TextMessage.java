package com.e_messenger.code.entity.message;

import com.e_messenger.code.entity.enums.GeneralType;

import java.time.LocalDateTime;

public class TextMessage extends Message{
    public TextMessage(String content, String actorId, String actorName, String conversationId, LocalDateTime time, String text) {
        super(content, GeneralType.TEXT, actorId, actorName, conversationId, time);
    }
}
