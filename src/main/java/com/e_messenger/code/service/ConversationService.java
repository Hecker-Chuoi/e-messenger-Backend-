package com.e_messenger.code.service;

import com.e_messenger.code.entity.Conversation;

import java.security.Principal;

public abstract class ConversationService {
    abstract public void deleteConversation(String conversationId, Principal principal);
    abstract public void checkAvailable(Conversation conv);
}
