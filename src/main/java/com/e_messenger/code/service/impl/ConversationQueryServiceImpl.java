package com.e_messenger.code.service.impl;

import com.e_messenger.code.entity.Participant;
import com.e_messenger.code.entity.enums.ConversationType;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.repository.ConversationRepository;
import com.e_messenger.code.service.ConversationQueryService;
import com.e_messenger.code.utils.ParticipantUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationQueryServiceImpl implements ConversationQueryService {
    UserService userService;
    ConversationRepository conversationRepo;
    ParticipantUtil participantUtil;

    static public String getDirectChatId(User curUser, User other){
        String id1 = curUser.getId();
        String id2 = other.getId();

        if(id1.compareTo(id2) > 0){
            String tmp = id1;
            id1 = id2;
            id2 = tmp;
        }
        return id1 + "-" + id2;
    }

    private String getDirectChatPattern(String id){
        return "^%s-|-%s$".formatted(id, id);
    }

    private String getDirectChatName(User curUser, List<String> ids){
        String otherId = ids.getFirst().equals(curUser.getId()) ? ids.getLast() : ids.getFirst();
        return userService.getUserById(otherId).getDisplayName();
    }

    // scenario: find other by email, use user's id to call this method to get conversation
    @Override
    public Conversation getDirectChat(String otherIdentifier){
        User curUser = userService.getCurrentUser();
        User other = userService.getUserByIdentifier(otherIdentifier);

        if(curUser.equals(other))
            throw new AppException(StatusCode.CONVERSATION_NOT_FOUND);

        Conversation result = conversationRepo.findConversationById(getDirectChatId(curUser, other)).orElseThrow(
                () -> new AppException(StatusCode.CONVERSATION_NOT_FOUND)
        );
        result.setConversationName(other.getDisplayName());
        result.setAvatarUrl(other.getAvatarUrl());

        return result;
    }

    @Override
    public Conversation getConversationById(String conversationId, String userId) {
        Conversation conv = conversationRepo.findConversationById(conversationId).orElseThrow(
                () -> new AppException(StatusCode.UNCATEGORIZED)
        );

        if(!participantUtil.hasConversationAccess(conv, userId))
            throw new AppException(StatusCode.UNCATEGORIZED);

        return conv;
    }

    @Override
    public List<Participant> getParticipants(String groupId) {
        Conversation group = getConversationById(groupId, userService.getCurrentUser().getId());
        return group.getParticipants();
    }

    @Override
    public List<Conversation> getAllDirectChat(int pageNum, int pageSize) {
        User curUser = userService.getCurrentUser();

        List<Conversation> result = conversationRepo.findAllDirectChat(getDirectChatPattern(curUser.getId()),
                PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.desc("lastMessageTime")))
        );
        for(Conversation conv : result){
            conv.setConversationName(getDirectChatName(curUser,
                    conv.getParticipants().stream().map(Participant::getParticipantId)
                            .collect(Collectors.toList())));
        }

        return result;
    }

    @Override
    public List<Conversation> getAllGroupChat(int pageNum, int pageSize) {
        User curUser = userService.getCurrentUser();

        return conversationRepo.findConversationByParticipantIdsContainingAndType(
                curUser.getId(), ConversationType.GROUP,
                PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.desc("lastMessageTime")))
        );
    }

    @Override
    public List<Conversation> getAllConversation(int pageNum, int pageSize) {
        User curUser = userService.getCurrentUser();

        List<Conversation> result =
                conversationRepo.findConversationByParticipantIdsContaining(curUser.getId(),
                    PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.desc("lastMessageTime")))
                );

        result.forEach(e -> {
            if(e.getType().equals(ConversationType.DIRECT))
                e.setConversationName(getDirectChatName(curUser,
                        e.getParticipants().stream().map(Participant::getParticipantId).collect(Collectors.toList())));
        });

        return result;
    }
}
