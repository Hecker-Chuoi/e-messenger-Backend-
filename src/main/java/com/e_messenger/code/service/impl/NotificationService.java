package com.e_messenger.code.service.impl;

import com.e_messenger.code.entity.Conversation;
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

    public void notifyNewMessage(List<Participant> participants, Message message) {
        for(Participant participant : participants) {
            messagingTemplate.convertAndSendToUser(
                    participant.getParticipantId(),
                    "/queue/messages",
                    messageMapper.toResponse(message)
            );
        }
    }

    public void notifyConversationUpdate(Conversation conversation) {

    }

    public void notifyGeneralNotifications() {

    }

    // new direct chat
    // someone left chat

    // new group chat
    // change conversation's info (name, image)
    // change role
        // any -> owner
        // member -> co owner
        // co owner -> member
    // change participants
        // add participants
        // remove participants
    // someone left group
    // owner delete group

    public void notifyNewConversation(List<Participant> participants, Conversation conversation) {
        for(Participant participant : participants) {
            messagingTemplate.convertAndSendToUser(
                    participant.getParticipantId(),
                    "/queue/conversations",
                    conversationMapper.toResponse(conversation)
            );
        }
    }

    public void notifyDeleteConversation(Conversation conversation) {
        for(Participant participant : conversation.getParticipants()) {
            messagingTemplate.convertAndSendToUser(
                    participant.getParticipantId(),
                    "/queue/conversations",
                    conversationMapper.toResponse(conversation)
            );
        }
    }

    public void notifyUpdateConversation(Conversation conversation) {
        for (Participant participant : conversation.getParticipants()) {
            messagingTemplate.convertAndSendToUser(
                    participant.getParticipantId(),
                    "/queue/conversations",
                    ""
            );
        }
    }
}