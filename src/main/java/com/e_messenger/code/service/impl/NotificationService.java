package com.e_messenger.code.service.impl;

import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.message.ConversationNotification;
import com.e_messenger.code.entity.message.Message;
import com.e_messenger.code.entity.Participant;
import com.e_messenger.code.mapstruct.ConversationMapper;
import com.e_messenger.code.mapstruct.MessageMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {
    SimpMessagingTemplate messagingTemplate;
    ConversationMapper conversationMapper;
    MessageMapper messageMapper;

    public void notifyNewMessage(Conversation conv, Message message) {
        for(Participant participant : conv.getParticipants()) {
            messagingTemplate.convertAndSendToUser(
                    participant.getParticipantId(),
                    "/messages",
                    message
            );
        }
    }

    public void notifyConversationUpdate(Conversation conv, ConversationNotification message) {
        for(Participant participant : conv.getParticipants()) {
            messagingTemplate.convertAndSendToUser(
                    participant.getParticipantId(),
                    "/conversations",
                    message
            );
        }
    }

    public void notifyGeneralNotifications() {

    }
}