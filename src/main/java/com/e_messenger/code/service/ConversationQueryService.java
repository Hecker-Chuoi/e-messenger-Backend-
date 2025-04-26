package com.e_messenger.code.service;

import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.Participant;
import com.e_messenger.code.entity.User;

import java.util.List;

public interface ConversationQueryService {
    Conversation getDirectChat(String otherIdentifier);
    Conversation getConversationById(String conversationId);
    List<Participant> getParticipants(String conversationId);
    List<Conversation> getAllDirectChat(int pageNum, int pageSize);
    List<Conversation> getAllGroupChat(int pageNum, int pageSize);
    List<Conversation> getAllConversation(int pageNum, int pageSize);
}
