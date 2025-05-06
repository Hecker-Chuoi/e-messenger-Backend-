package com.e_messenger.code.service.impl;

import com.e_messenger.code.entity.enums.ConversationType;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.repository.ConversationRepository;
import com.e_messenger.code.service.DirectChatService;
import com.e_messenger.code.utils.ParticipantUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class DirectChatServiceImpl extends DirectChatService {
    ConversationRepository conversationRepo;
    UserService userService;
    ParticipantUtil participantUtil;
    ConversationQueryServiceImpl queryService;

    @Override
    public Conversation createDirectChat(String otherId) {
        User curUser = userService.getCurrentUser();
        User other = userService.getUserByIdentifier(otherId);

        if(curUser.equals(other))
            throw new AppException(StatusCode.UNCATEGORIZED);

        try{
            queryService.getDirectChat(otherId);
            throw new AppException(StatusCode.CONVERSATION_ALREADY_EXISTS);
        }
        catch (AppException e){
            if(e.getStatusCode().equals(StatusCode.CONVERSATION_ALREADY_EXISTS)){
                throw e;
            }
        }

        Conversation newDirect = Conversation.builder()
                .id(ConversationQueryServiceImpl.getDirectChatId(curUser, other))
                .type(ConversationType.DIRECT)
                .participants(participantUtil.toDirectParticipants(curUser, other))
                .build();

        return conversationRepo.save(newDirect);
    }

    @Override
    public boolean leaveConversation(String conversationId) {
        Conversation direct = queryService.getConversationById(conversationId, userService.getCurrentUser().getId());
        if(direct.getType().equals(ConversationType.DIRECT)){
            conversationRepo.delete(direct);
            return true;
        }
        throw new AppException(StatusCode.UNCATEGORIZED);
    }
}
