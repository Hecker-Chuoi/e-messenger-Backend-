package com.e_messenger.code.service.impl;

import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.repository.ConversationRepository;
import com.e_messenger.code.service.ConversationQueryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationQueryServiceImpl implements ConversationQueryService {
    UserService userService;
    ConversationRepository conversationRepo;

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
            throw new AppException(StatusCode.UNCATEGORIZED);

        Conversation result = conversationRepo.findConversationById(getDirectChatId(curUser, other)).orElseThrow(
                () -> new AppException(StatusCode.UNCATEGORIZED)
        );
        result.setConversationName(other.getDisplayName());

        return result;
    }

    @Override
    @PostAuthorize("returnObject.participantIds.contains(authentication.name)")
    public Conversation getConversationById(String conversationId) {
        return conversationRepo.findConversationById(conversationId).orElseThrow(
                () -> new AppException(StatusCode.UNCATEGORIZED)
        );
    }

    @Override
    public List<Conversation> getAllDirectChat(int pageNum, int pageSize) {
        User curUser = userService.getCurrentUser();

        List<Conversation> result = conversationRepo.findAllDirectChat(getDirectChatPattern(curUser.getId()),
                PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.desc("lastMessageTime")))
        );
        for(Conversation conv : result){
            conv.setConversationName(getDirectChatName(curUser, conv.getParticipantIds()));
        }

        return result;
    }

    @Override
    public List<Conversation> getAllGroupChat(int pageNum, int pageSize) {
        User curUser = userService.getCurrentUser();

        return conversationRepo.findConversationByParticipantIdsContainingAndType(
                curUser.getId(), Conversation.ConversationType.GROUP,
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
            if(e.getType().equals(Conversation.ConversationType.DIRECT))
                e.setConversationName(getDirectChatName(curUser, e.getParticipantIds()));
        });

        return result;
    }
}
