package com.e_messenger.code.service;

import com.e_messenger.code.dto.requests.conv.GroupCreationRequest;
import com.e_messenger.code.dto.requests.conv.GroupUpdateRequest;
import com.e_messenger.code.entity.Conversation;

import java.security.Principal;
import java.util.List;

public abstract class GroupChatService extends ConversationService{
    abstract public Conversation createGroupChat(GroupCreationRequest request, Principal principal);
    abstract public Conversation changeName(String groupId, String newName, Principal principal);
    abstract public Conversation addParticipants(String groupId, List<String> participantIds, Principal principal);
    abstract public Conversation removeParticipants(String groupId, List<String> participantIds, Principal principal);
    abstract public void deleteGroup(String groupId, Principal principal);
    abstract public Conversation setOwner(String groupId, String ownerId, Principal principal);
    abstract public Conversation setCoOwner(String groupId, List<String> coOwnerIds, Principal principal);
    abstract public Conversation toMember(String groupId, List<String> participantIds, Principal principal);
}
