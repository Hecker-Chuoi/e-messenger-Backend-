package com.e_messenger.code.service.impl;

import com.e_messenger.code.entity.Participant;
import com.e_messenger.code.entity.enums.ConversationRole;
import com.e_messenger.code.entity.enums.ConversationType;
import com.e_messenger.code.dto.requests.GroupCreationRequest;
import com.e_messenger.code.dto.requests.GroupUpdateRequest;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.mapstruct.ConversationMapper;
import com.e_messenger.code.repository.ConversationRepository;
import com.e_messenger.code.service.ConversationQueryService;
import com.e_messenger.code.service.GroupChatService;
import com.e_messenger.code.utils.ParticipantUtil;
import com.e_messenger.code.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class GroupChatServiceImpl extends GroupChatService {
    ConversationRepository conversationRepo;
    ConversationMapper conversationMapper;
    ConversationQueryService queryService;
    UserService userService;
    UserUtil userUtil;
    ParticipantUtil participantUtil;
    ConversationQueryService conversationQueryService;

    private <T> List<T> addAll(List<T> list, List<T> items){
        list.addAll(items);
        return list;
    }

    private List<Participant> removeAll(List<Participant> list, List<String> items){
        Set<String> set = new LinkedHashSet<>(items);
        list.removeIf(e -> set.contains(e.getParticipantId()));
        return list;
    }

    @Override
    public Conversation createGroupChat(GroupCreationRequest request) {
        List<User> validUsers = userUtil.getValidUsers(request.getParticipantIds());

        if(validUsers.size() < 3)
            throw new AppException(StatusCode.UNCATEGORIZED);

        Conversation group = conversationMapper.toEntity(request);

        if(!request.getParticipantIds().getFirst().equals(userService.getCurrentUser().getId())) {
            throw new AppException(StatusCode.UNCATEGORIZED);
        }

        group.setParticipants(participantUtil.toGroupParticipants(validUsers));
        group.setType(ConversationType.GROUP);
        group.setLastMessageTime(LocalDateTime.now());
        group.setId(UUID.randomUUID().toString());
        return conversationRepo.save(group);
    }

    @Override
    @PreAuthorize("@participantUtil.hasRole(@conversationQueryServiceImpl.getConversationById(#groupId), @userService.getCurrentUser(), T(com.e_messenger.code.entity.enums.ConversationRole).OWNER)")
    public Conversation updateGroupInfo(String groupId, GroupUpdateRequest request) {
        Conversation group = conversationRepo.findConversationById(groupId).orElseThrow(
                () -> new AppException(StatusCode.UNCATEGORIZED)
        );
        conversationMapper.update(group, request);
        return conversationRepo.save(group);
    }

    @Override
    public Conversation addParticipants(String groupId, List<String> participantIds) {
        User curUser = userService.getCurrentUser();
        Conversation group = queryService.getConversationById(groupId, curUser.getId());

        if(!participantUtil.hasManagementRole(group, curUser))
            throw new AppException(StatusCode.UNCATEGORIZED);

        LinkedHashSet<User> curUserList = new LinkedHashSet<>(participantUtil.toUsers(group.getParticipants()));
        List<User> validUsers = userUtil.getValidUsers(participantIds);

        if(validUsers.stream().anyMatch(curUserList::contains)){
            throw new AppException(StatusCode.UNCATEGORIZED);
        }

        group.setParticipants(
                addAll(group.getParticipants(), participantUtil.toMembers(validUsers))
        );

        return conversationRepo.save(group);
    }

    @Override
    public Conversation removeParticipants(String groupId, List<String> removeIds) {
        User curUser = userService.getCurrentUser();
        Conversation group = queryService.getConversationById(groupId, curUser.getId());

        if(!participantUtil.canAffect(group, curUser, removeIds))
            throw new AppException(StatusCode.UNCATEGORIZED);

        group.setParticipants(
                removeAll(group.getParticipants(), removeIds)
        );
        if(group.getParticipants().isEmpty())
            throw new AppException(StatusCode.UNCATEGORIZED);

        return conversationRepo.save(group);
    }

    @Override
    public void deleteGroup(String groupId) {
        User curUser = userService.getCurrentUser();
        Conversation group = conversationQueryService.getConversationById(groupId, curUser.getId());

        if(!participantUtil.hasRole(group, curUser, ConversationRole.OWNER))
            throw new AppException(StatusCode.UNCATEGORIZED);

        conversationRepo.delete(group);
    }

    @Override
    public Conversation setOwner(String groupId, String newOwnerId) {
        User curUser = userService.getCurrentUser();
        Conversation group = conversationQueryService.getConversationById(groupId, curUser.getId());

        if(!participantUtil.hasRole(group, curUser, ConversationRole.OWNER))
            throw new AppException(StatusCode.UNCATEGORIZED);

        participantUtil.changeOwner(group, userService.getCurrentUser().getId(), newOwnerId);
        return conversationRepo.save(group);
    }

    @Override
    public Conversation setCoOwner(String groupId, List<String> coOwnerIds) {
        User curUser = userService.getCurrentUser();
        Conversation group = conversationQueryService.getConversationById(groupId, curUser.getId());

        if(!participantUtil.hasRole(group, curUser, ConversationRole.OWNER))
            throw new AppException(StatusCode.UNCATEGORIZED);

        List<User> validUsers = userUtil.getValidUsers(coOwnerIds);

        participantUtil.setCoOwners(group, validUsers);
        return conversationRepo.save(group);
    }

    @Override
    public Conversation toMember(String groupId, List<String> participantIds) {
        User curUser = userService.getCurrentUser();
        Conversation group = conversationQueryService.getConversationById(groupId, curUser.getId());

        if(!participantUtil.canAffect(group, curUser, participantIds))
            throw new AppException(StatusCode.UNCATEGORIZED);

        List<User> validUsers = userUtil.getValidUsers(participantIds);
        participantUtil.setMembers(group, validUsers);
        return conversationRepo.save(group);
    }

    @Override
    public boolean leaveConversation(String groupId) {
        User curUser = userService.getCurrentUser();
        Conversation group = queryService.getConversationById(groupId, curUser.getId());

        if(group.getParticipants().size() == 1)
            throw new AppException(StatusCode.UNCATEGORIZED);
        if(participantUtil.getParticipantRole(group, curUser.getId()).equals(ConversationRole.OWNER))
            throw new AppException(StatusCode.UNCATEGORIZED);

        group.setParticipants(
                removeAll(group.getParticipants(), List.of(curUser.getId()))
        );

        conversationRepo.save(group);
        return true;
    }
}
