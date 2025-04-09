package com.e_messenger.code.service;

import com.e_messenger.code.dto.requests.MessageRequest;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.Message;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class DirectMessageService {
    UserService userService;
    ConversationService conversationService;
    MessageRepository messageRepo;

    @Transactional
    public Message sendDirectMessage(String otherId, MessageRequest request){
        User sender = userService.getMyInfo();
        Conversation direct;
        try{
            direct = conversationService.getDirectChat(otherId);
        }
        catch(AppException e){
            direct = conversationService.createDirectChat(otherId);
        }

        Message newMessage = Message.builder()
                .text(request.getText())
                .conversationId(direct.getId())
                .senderId(sender.getId())
                .senderName(sender.getDisplayName())
                .sentAt(LocalDateTime.now())
                .build();

        conversationService.updateLastSentInfo(direct, newMessage);
        return messageRepo.save(newMessage);
    }

    @Transactional
    public Message sendMessageToConversation(String conversationId, MessageRequest request){
        User sender = userService.getMyInfo();
        Conversation conversation = conversationService.getConversation(conversationId);

        Message newMessage = Message.builder()
                .text(request.getText())
                .conversationId(conversation.getId())
                .senderId(sender.getId())
                .senderName(sender.getDisplayName())
                .sentAt(LocalDateTime.now())
                .build();

        conversationService.updateLastSentInfo(conversation, newMessage);
        return messageRepo.save(newMessage);
    }

    public List<Message> getDirectMessages(String conversationId){
        return messageRepo.getMessagesByConversationId(conversationId);
    }
}
