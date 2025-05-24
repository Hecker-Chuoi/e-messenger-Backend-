package com.e_messenger.code.entity.message;

import com.e_messenger.code.entity.enums.GeneralType;

import java.time.LocalDateTime;

public class MediaMessage extends Message{
    String contentType;
    String url;

    public MediaMessage(GeneralType type, String actorId, String actorName, String conversationId, LocalDateTime time, String contentType, String url) {
        super("[Image]", type, actorId, actorName, conversationId, time);
        this.contentType = contentType;
        this.url = url;
    }
}
