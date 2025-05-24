package com.e_messenger.code.controller;

import com.e_messenger.code.dto.requests.MessageRequest;
import com.e_messenger.code.dto.responses.ApiResponse;
import com.e_messenger.code.dto.responses.MessageResponse;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.message.Message;
import com.e_messenger.code.entity.enums.GeneralType;
import com.e_messenger.code.mapstruct.MessageMapper;
import com.e_messenger.code.service.ConversationQueryService;
import com.e_messenger.code.service.impl.ChattingService;
import com.e_messenger.code.service.impl.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@SecurityRequirement(name = "user token")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/chat")
@RestController
public class ChattingController {
    ChattingService mainService;
    MessageMapper messageMapper;
    NotificationService notificationService;
    ConversationQueryService conversationQueryService;

    @MessageMapping("/{conversationId}/send-message")
    public void sendMessage(@DestinationVariable String conversationId, @Payload MessageRequest request, Principal principal) throws IOException {
        GeneralType type = request.getType();
        Message result = null;

        Conversation conv = conversationQueryService.getConversationById(conversationId, principal.getName());
        if(type.equals(GeneralType.TEXT))
            result = mainService.sendText(conv, request, principal);
        else
            result = mainService.sendFile(conv, request, principal);

        notificationService.notifyNewMessage(conv.getParticipants(), result);
    }

    @GetMapping("/histories/{conversationId}")
    public ApiResponse<List<MessageResponse>> getMessageHistory(
            @PathVariable String conversationId,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize
    ){
        List<Message> messages = mainService.getMessageHistory(conversationId, pageNum, pageSize);
        return ApiResponse.<List<MessageResponse>>builder()
                .result(messageMapper.toResponses(messages))
                .build();
    }
}
