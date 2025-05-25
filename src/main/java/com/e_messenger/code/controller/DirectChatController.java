package com.e_messenger.code.controller;

import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.mapstruct.ConversationMapper;
import com.e_messenger.code.service.ConversationQueryService;
import com.e_messenger.code.service.DirectChatService;
import com.e_messenger.code.service.impl.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/direct")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "user token")
public class DirectChatController {
    DirectChatService service;
    ConversationMapper conversationMapper;
    ConversationQueryService conversationQueryService;
    NotificationService notificationService;

    @MessageMapping("/direct/create/{otherId}")
    public void createDirectChat(@DestinationVariable String otherId, Principal principal) {
        Conversation result = service.createDirectChat(otherId, principal);

    }

    @MessageMapping("/direct/{conversationId}/delete")
    public void deleteConversation(@DestinationVariable String conversationId, Principal principal) {
        service.deleteConversation(conversationId, principal);
    }
}