package com.e_messenger.code.service;

import com.e_messenger.code.entity.Conversation;

import java.util.List;

public interface ConversationQueryService {
    Conversation getDirectChat(String otherIdentifier);
    Conversation getConversationById(String conversationId);
    List<Conversation> getAllDirectChat(int pageNum, int pageSize);
    List<Conversation> getAllGroupChat(int pageNum, int pageSize);
    List<Conversation> getAllConversation(int pageNum, int pageSize);
}
