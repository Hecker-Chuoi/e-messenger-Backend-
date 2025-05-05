package com.e_messenger.code.service.impl;

import com.e_messenger.code.dto.requests.MessageRequest;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.Message;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.mapstruct.ConversationMapper;
import com.e_messenger.code.mapstruct.MessageMapper;
import com.e_messenger.code.repository.ConversationRepository;
import com.e_messenger.code.repository.MessageRepository;
import com.e_messenger.code.service.ConversationQueryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChattingService {
    MessageRepository mainRepo;
    ConversationRepository conversationRepo;

    MessageMapper messageMapper;
    ConversationMapper conversationMapper;

    UserService userService;
    NotificationService notificationService;
    ConversationQueryServiceImpl conversationQueryService;

    public Message sendMessage(String conversationId, MessageRequest request){
        User curUser = userService.getCurrentUser();
        Conversation conversation = conversationQueryService.getConversationById(conversationId);

        Message message = Message.builder()
                .senderId(curUser.getId())
                .senderName(curUser.getDisplayName())
                .conversationId(conversationId)
                .sentAt(LocalDateTime.now())
                .build();

        messageMapper.update(message, request);
        conversationMapper.updateLastSentInfo(conversation, message);

        notificationService.notifyNewMessage(conversation, messageMapper.toResponse(message));

        conversationRepo.save(conversation);
        return mainRepo.save((message));
    }

    public List<Message> getMessageHistory(String conversationId, int pageNum, int pageSize){
        Conversation conversation = conversationQueryService.getConversationById(conversationId);
        return mainRepo.findMessagesByConversationId(conversationId,
                PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.desc("sentAt"))))
                .stream().toList();
    }
}
