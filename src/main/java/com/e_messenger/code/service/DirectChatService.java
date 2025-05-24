package com.e_messenger.code.service;

import com.e_messenger.code.dto.requests.MessageRequest;
import com.e_messenger.code.entity.Conversation;

import java.security.Principal;

public abstract class DirectChatService extends ConversationService{
    abstract public Conversation createDirectChat(String otherId, Principal principal);
}
