package com.e_messenger.code.service;

import com.e_messenger.code.dto.requests.GroupCreationRequest;
import com.e_messenger.code.dto.requests.GroupUpdateRequest;
import com.e_messenger.code.entity.Conversation;

import java.util.List;

public abstract class GroupChatService extends ConversationService{
    abstract public Conversation createGroupChat(GroupCreationRequest request);
    abstract public Conversation updateGroupInfo(String groupId, GroupUpdateRequest request);
    abstract public Conversation addParticipants(String groupId, List<String> participantIds);
    abstract public Conversation removeParticipants(String groupId, List<String> participantIds);
}
