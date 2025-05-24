package com.e_messenger.code.service;

import com.e_messenger.code.entity.Conversation;

import java.security.Principal;

public abstract class ConversationService {
    abstract public Conversation leaveConversation(String conversationId, Principal principal);
}
