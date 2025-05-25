package com.e_messenger.code.service.impl;

import com.e_messenger.code.entity.Participant;
import com.e_messenger.code.entity.enums.ConversationRole;
import com.e_messenger.code.entity.enums.ConversationType;
import com.e_messenger.code.dto.requests.conv.GroupCreationRequest;
import com.e_messenger.code.dto.requests.conv.GroupUpdateRequest;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.entity.message.ConversationNotification;
import com.e_messenger.code.entity.message.conversation.general.ConversationCreation;
import com.e_messenger.code.entity.message.conversation.general.ConversationDeletion;
import com.e_messenger.code.entity.message.conversation.general.LeaveConversation;
import com.e_messenger.code.entity.message.conversation.update.ChangeName;
import com.e_messenger.code.entity.message.conversation.update.ChangeParticipant;
import com.e_messenger.code.entity.message.conversation.update.ChangeRole;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.mapstruct.ConversationMapper;
import com.e_messenger.code.repository.ConversationRepository;
import com.e_messenger.code.repository.MessageRepository;
import com.e_messenger.code.service.ConversationQueryService;
import com.e_messenger.code.service.GroupChatService;
import com.e_messenger.code.utils.ParticipantUtil;
import com.e_messenger.code.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class GroupChatServiceImpl extends GroupChatService {
    UserService userService;
    ConversationQueryService queryService;
    NotificationService notificationService;
    ConversationQueryService conversationQueryService;

    ConversationRepository conversationRepo;
    MessageRepository messageRepo;

    ConversationMapper conversationMapper;

    UserUtil userUtil;
    ParticipantUtil participantUtil;

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
    public Conversation createGroupChat(GroupCreationRequest request, Principal principal) {
        List<User> validUsers = userUtil.getValidUsers(request.getParticipantIds());

        if(validUsers.size() < 3)
            throw new AppException(StatusCode.UNCATEGORIZED);

        User actor = userService.getUserById(principal.getName());
        Conversation group = conversationMapper.toEntity(request);

        if(!request.getParticipantIds().getFirst().equals(actor.getId())) {
            throw new AppException(StatusCode.UNCATEGORIZED);
        }

        group.setParticipants(participantUtil.toGroupParticipants(validUsers));
        group.setType(ConversationType.GROUP);
        group.setLastMessageTime(LocalDateTime.now());
        group.setId(UUID.randomUUID().toString());

        ConversationCreation message = ConversationCreation.builder()
                .name(group.getConversationName())
                .actorId(actor.getId())
                .actorName(actor.getDisplayName())
                .conversationId(group.getId())
                .time(LocalDateTime.now())
                .build();

        messageRepo.save(message);

        conversationMapper.updateLastSentInfo(group, message);
        group =  conversationRepo.save(group);

        notificationService.notifyConversationUpdate(group, message);
        return group;
    }

    @Override
    public Conversation changeName(String groupId, String newName, Principal principal) {
        User actor = userService.getUserById(principal.getName());
        Conversation group = conversationQueryService.getConversationById(groupId, principal.getName());

        if(!participantUtil.hasRole(group, userService.getUserById(principal.getName()), ConversationRole.OWNER))
            throw new AppException(StatusCode.UNCATEGORIZED);

        ChangeName message = ChangeName.builder()
                .oldName(group.getConversationName())
                .newName(newName)
                .actorId(actor.getId())
                .actorName(actor.getDisplayName())
                .conversationId(group.getId())
                .time(LocalDateTime.now())
                .build();

        group.setConversationName(newName);

        messageRepo.save(message);

        conversationMapper.updateLastSentInfo(group, message);
        conversationRepo.save(group);

        notificationService.notifyConversationUpdate(group, message);
        return group;
    }

    @Override
    public Conversation addParticipants(String groupId, List<String> participantIds, Principal principal) {
        User actor = userService.getUserById(principal.getName());
        Conversation group = queryService.getConversationById(groupId, principal.getName());

        if(!participantUtil.hasManagementRole(group, actor))
            throw new AppException(StatusCode.UNCATEGORIZED);

        LinkedHashSet<User> curUserList = new LinkedHashSet<>(participantUtil.toUsers(group.getParticipants()));
        List<User> validUsers = userUtil.getValidUsers(participantIds);

        if(validUsers.stream().anyMatch(curUserList::contains)){
            throw new AppException(StatusCode.UNCATEGORIZED);
        }

        group.setParticipants(
                addAll(group.getParticipants(), participantUtil.toMembers(validUsers))
        );

        ChangeParticipant message = ChangeParticipant.builder()
                .affectedParticipants(participantIds)
                .method(ChangeParticipant.Method.ADD)
                .actorId(actor.getId())
                .actorName(actor.getDisplayName())
                .conversationId(group.getId())
                .time(LocalDateTime.now())
                .build();

        messageRepo.save(message);

        conversationMapper.updateLastSentInfo(group, message);
        conversationRepo.save(group);

        notificationService.notifyConversationUpdate(group, message);
        return group;
    }

    @Override
    public Conversation removeParticipants(String groupId, List<String> removeIds, Principal principal) {
        User actor = userService.getUserById(principal.getName());
        Conversation group = queryService.getConversationById(groupId, principal.getName());

        if(!participantUtil.canAffect(group, actor, removeIds))
            throw new AppException(StatusCode.UNCATEGORIZED);

        group.setParticipants(
                removeAll(group.getParticipants(), removeIds)
        );
        if(group.getParticipants().isEmpty())
            throw new AppException(StatusCode.UNCATEGORIZED);

        ChangeParticipant message = ChangeParticipant.builder()
                .affectedParticipants(removeIds)
                .method(ChangeParticipant.Method.REMOVE)
                .actorId(actor.getId())
                .actorName(actor.getDisplayName())
                .conversationId(group.getId())
                .time(LocalDateTime.now())
                .build();

        messageRepo.save(message);

        conversationMapper.updateLastSentInfo(group, message);
        conversationRepo.save(group);

        notificationService.notifyConversationUpdate(group, message);
        return group;
    }

    @Override
    public Conversation setOwner(String groupId, String newOwnerId, Principal principal) {
        User actor = userService.getUserById(principal.getName());
        Conversation group = conversationQueryService.getConversationById(groupId, principal.getName());

        if(!participantUtil.hasRole(group, actor, ConversationRole.OWNER))
            throw new AppException(StatusCode.UNCATEGORIZED);

        participantUtil.changeOwner(group, principal.getName(), newOwnerId);

        ChangeRole message = ChangeRole.builder()
                .fromRole(null)
                .toRole(ConversationRole.OWNER)
                .affectedParticipants(List.of(newOwnerId))
                .actorId(actor.getId())
                .actorName(actor.getDisplayName())
                .conversationId(group.getId())
                .time(LocalDateTime.now())
                .build();

        messageRepo.save(message);

        conversationMapper.updateLastSentInfo(group, message);
        conversationRepo.save(group);

        notificationService.notifyConversationUpdate(group, message);
        return group;
    }

    @Override
    public Conversation setCoOwner(String groupId, List<String> coOwnerIds, Principal principal) {
        User actor = userService.getUserById(principal.getName());
        Conversation group = conversationQueryService.getConversationById(groupId, principal.getName());

        if(!participantUtil.hasRole(group, actor, ConversationRole.OWNER))
            throw new AppException(StatusCode.UNCATEGORIZED);

        List<User> validUsers = userUtil.getValidUsers(coOwnerIds);

        participantUtil.setCoOwners(group, validUsers);

        ChangeRole message = ChangeRole.builder()
                .fromRole(ConversationRole.MEMBER)
                .toRole(ConversationRole.CO_OWNER)
                .affectedParticipants(coOwnerIds)
                .actorId(actor.getId())
                .actorName(actor.getDisplayName())
                .conversationId(group.getId())
                .time(LocalDateTime.now())
                .build();

        messageRepo.save(message);

        conversationMapper.updateLastSentInfo(group, message);
        conversationRepo.save(group);

        notificationService.notifyConversationUpdate(group, message);
        return group;
    }

    @Override
    public Conversation toMember(String groupId, List<String> participantIds, Principal principal) {
        User actor = userService.getUserById(principal.getName());
        Conversation group = conversationQueryService.getConversationById(groupId, principal.getName());

        if(!participantUtil.canAffect(group, actor, participantIds))
            throw new AppException(StatusCode.UNCATEGORIZED);

        List<User> validUsers = userUtil.getValidUsers(participantIds);
        participantUtil.setMembers(group, validUsers);

        ChangeRole message = ChangeRole.builder()
                .fromRole(ConversationRole.CO_OWNER)
                .toRole(ConversationRole.MEMBER)
                .affectedParticipants(participantIds)
                .actorId(actor.getId())
                .actorName(actor.getDisplayName())
                .conversationId(group.getId())
                .time(LocalDateTime.now())
                .build();

        messageRepo.save(message);

        conversationMapper.updateLastSentInfo(group, message);
        conversationRepo.save(group);

        notificationService.notifyConversationUpdate(group, message);
        return group;
    }

    @Override
    public Conversation leaveGroup(String groupId, Principal principal) {
        User actor = userService.getUserById(principal.getName());
        Conversation group = queryService.getConversationById(groupId, principal.getName());

        if(group.getParticipants().size() == 1)
            throw new AppException(StatusCode.UNCATEGORIZED);
        if(participantUtil.getParticipantRole(group, principal.getName()).equals(ConversationRole.OWNER))
            throw new AppException(StatusCode.UNCATEGORIZED);

        group.setParticipants(
                removeAll(group.getParticipants(), List.of(principal.getName()))
        );

        LeaveConversation message = LeaveConversation.builder()
                .actorId(actor.getId())
                .actorName(actor.getDisplayName())
                .conversationId(group.getId())
                .time(LocalDateTime.now())
                .build();

        messageRepo.save(message);

        conversationMapper.updateLastSentInfo(group, message);
        conversationRepo.save(group);

        notificationService.notifyConversationUpdate(group, message);
        return group;
    }

    @Override
    public void deleteConversation(String groupId, Principal principal) {
        User actor = userService.getUserById(principal.getName());
        Conversation group = conversationQueryService.getConversationById(groupId, principal.getName());

        if(!participantUtil.hasRole(group, actor, ConversationRole.OWNER))
            throw new AppException(StatusCode.UNCATEGORIZED);

        ConversationDeletion message = ConversationDeletion.builder()
                .name(group.getConversationName())
                .actorId(actor.getId())
                .actorName(actor.getDisplayName())
                .conversationId(group.getId())
                .time(LocalDateTime.now())
                .build();

        notificationService.notifyConversationUpdate(group, message);
        conversationRepo.delete(group);
    }
}
