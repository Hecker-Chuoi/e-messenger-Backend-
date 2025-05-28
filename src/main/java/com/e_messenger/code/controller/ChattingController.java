package com.e_messenger.code.controller;

import com.e_messenger.code.dto.requests.message.MediaMessageRequest;
import com.e_messenger.code.dto.requests.message.TextMessageRequest;
import com.e_messenger.code.dto.responses.ApiResponse;
import com.e_messenger.code.dto.responses.MessageResponse;
import com.e_messenger.code.entity.message.Message;
import com.e_messenger.code.mapstruct.MessageMapper;
import com.e_messenger.code.service.impl.ChattingService;
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

    @MessageMapping("/{conversationId}/send-text")
    public void sendText(@DestinationVariable String conversationId, @Payload TextMessageRequest request, Principal principal) throws IOException {
        Message result = mainService.sendText(conversationId, request, principal);
    }

    @MessageMapping("/{conversationId}/send-media")
    public void sendMedia(@DestinationVariable String conversationId, @Payload MediaMessageRequest request, Principal principal) throws IOException {
        Message result = mainService.sendFile(conversationId, request, principal);
        System.out.println(result);
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
