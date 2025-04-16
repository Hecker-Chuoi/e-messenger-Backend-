package com.e_messenger.code.service.impl;

import com.e_messenger.code.dto.requests.GroupCreationRequest;
import com.e_messenger.code.dto.requests.GroupUpdateRequest;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.mapstruct.ConversationMapper;
import com.e_messenger.code.repository.ConversationRepository;
import com.e_messenger.code.repository.MessageRepository;
import com.e_messenger.code.service.ConversationQueryService;
import com.e_messenger.code.service.GroupChatService;
import com.e_messenger.code.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    private <T> List<T> addAll(List<T> list, List<T> items){
        list.addAll(items);
        return list;
    }

    private <T> List<T> removeAll(List<T> list, List<T> items){
        list.removeAll(items);
        return list;
    }

    @Override
    public Conversation createGroupChat(GroupCreationRequest request) {
        if(request.getParticipantIds().size() < 3)
            throw new AppException(StatusCode.UNCATEGORIZED);
        Conversation group = conversationMapper.toEntity(request);
        group.setType(Conversation.ConversationType.GROUP);

        List<String> notValidIds = userUtil.findNotValidUserId(request.getParticipantIds());
        if(!notValidIds.isEmpty()) {
            throw new AppException(StatusCode.UNCATEGORIZED);
        }

        group.setLastMessageTime(LocalDateTime.now());
        group.setId(UUID.randomUUID().toString());
        return conversationRepo.save(group);
    }

    @Override
    public Conversation updateGroupInfo(String groupId, GroupUpdateRequest request) {
        Conversation group = conversationRepo.findConversationById(groupId).orElseThrow(
                () -> new AppException(StatusCode.UNCATEGORIZED)
        );
        conversationMapper.update(group, request);
        return conversationRepo.save(group);
    }

    @Override
    public Conversation addParticipants(String groupId, List<String> participantIds) {
        Conversation group = queryService.getConversationById(groupId);
        group.setParticipantIds(
                addAll(group.getParticipantIds(), participantIds)
        );

        return conversationRepo.save(group);
    }

    @Override
    public Conversation removeParticipants(String groupId, List<String> participantIds) {
        Conversation group = queryService.getConversationById(groupId);
        group.setParticipantIds(
                removeAll(group.getParticipantIds(), participantIds)
        );
        if(group.getParticipantIds().isEmpty())
            throw new AppException(StatusCode.UNCATEGORIZED);

        return conversationRepo.save(group);
    }

    @Override
    public boolean leaveConversation(String groupId) {
        User curUser = userService.getCurrentUser();
        Conversation group = queryService.getConversationById(groupId);
        if(group.getParticipantIds().size() == 1)
            throw new AppException(StatusCode.UNCATEGORIZED);

        group.setParticipantIds(
                removeAll(group.getParticipantIds(), List.of(curUser.getId())   )
        );
        if(group.getParticipantIds().isEmpty())
            throw new AppException(StatusCode.UNCATEGORIZED);
        conversationRepo.save(group);
        return true;
    }
}
