package com.e_messenger.code.service.impl;

import com.e_messenger.code.entity.enums.ConversationType;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.entity.message.conversation.general.ConversationCreation;
import com.e_messenger.code.entity.message.conversation.general.ConversationDeletion;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.mapstruct.ConversationMapper;
import com.e_messenger.code.repository.ConversationRepository;
import com.e_messenger.code.repository.MessageRepository;
import com.e_messenger.code.service.DirectChatService;
import com.e_messenger.code.utils.ParticipantUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class DirectChatServiceImpl extends DirectChatService {
    ConversationRepository conversationRepo;
    MessageRepository messageRepo;

    UserService userService;
    ConversationQueryServiceImpl queryService;

    ParticipantUtil participantUtil;

    ConversationMapper conversationMapper;
    NotificationService notificationService;

    @Override
    public Conversation createDirectChat(String otherId, Principal principal) {
        User actor = userService.getUserById(principal.getName());
        User other = userService.getUserByIdentifier(otherId);

        if(actor.equals(other))
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

        Conversation direct = Conversation.builder()
                .id(ConversationQueryServiceImpl.getDirectChatId(actor, other))
                .type(ConversationType.DIRECT)
                .participants(participantUtil.toDirectParticipants(actor, other))
                .build();

        ConversationCreation message = ConversationCreation.builder()
                .content("Conversation created")
                .name(direct.getConversationName())
                .actorId(actor.getId())
                .actorName(actor.getDisplayName())
                .conversationId(direct.getId())
                .time(Instant.now())
                .build();

        messageRepo.save(message);

        conversationMapper.updateLastSentInfo(direct, message);
        conversationRepo.save(direct);

        notificationService.notifyConversationUpdate(direct, message);

        return direct;
    }

    @Override
    public void deleteConversation(String conversationId, Principal principal) {
        User actor = userService.getUserById(principal.getName());
        Conversation direct = queryService.getConversationById(conversationId, userService.getCurrentUser().getId());

        if(direct.getType().equals(ConversationType.DIRECT)){
            ConversationDeletion message = ConversationDeletion.builder()
                .content("Conversation deleted")
                .name(direct.getConversationName())
                .actorId(actor.getId())
                .actorName(actor.getDisplayName())
                .conversationId(direct.getId())
                .time(Instant.now())
                .build();

            conversationRepo.delete(direct);

            notificationService.notifyConversationUpdate(direct, message);
        }
        throw new AppException(StatusCode.UNCATEGORIZED);
    }
}
