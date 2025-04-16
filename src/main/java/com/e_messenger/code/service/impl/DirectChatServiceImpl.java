package com.e_messenger.code.service.impl;

import com.e_messenger.code.dto.requests.MessageRequest;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.Message;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.mapstruct.ConversationMapper;
import com.e_messenger.code.mapstruct.MessageMapper;
import com.e_messenger.code.repository.ConversationRepository;
import com.e_messenger.code.repository.MessageRepository;
import com.e_messenger.code.service.DirectChatService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class DirectChatServiceImpl extends DirectChatService {
    ConversationRepository conversationRepo;
    UserService userService;
    ConversationQueryServiceImpl queryService;

    @Override
    public Conversation createDirectChat(String otherId) {
        User curUser = userService.getCurrentUser();
        User other = userService.getUserByIdentifier(otherId);

        Conversation newDirect = Conversation.builder()
                .id(ConversationQueryServiceImpl.getDirectChatId(curUser, other))
                .type(Conversation.ConversationType.DIRECT)
                .participantIds(List.of(curUser.getId(), other.getId()))
                .build();

        return conversationRepo.save(newDirect);
    }

    @Override
    public boolean leaveConversation(String conversationId) {
        Conversation direct = queryService.getConversationById(conversationId);
        if(direct.getType().equals(Conversation.ConversationType.DIRECT)){
            conversationRepo.delete(direct);
            return true;
        }
        throw new AppException(StatusCode.UNCATEGORIZED);
    }
}
