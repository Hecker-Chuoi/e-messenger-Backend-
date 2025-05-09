package com.e_messenger.code.service.impl;

import com.e_messenger.code.dto.responses.MessageResponse;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.Participant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {
    SimpMessagingTemplate messagingTemplate;

    public void notifyNewMessage(Conversation conversation, MessageResponse response) {
        for(Participant participant : conversation.getParticipants()) {
            messagingTemplate.convertAndSendToUser(
                    participant.getParticipantId(),
                    "/queue/messages",
                    response
            );
        }
    }
}
