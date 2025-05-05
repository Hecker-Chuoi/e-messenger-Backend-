package com.e_messenger.code.controller;

import com.e_messenger.code.dto.requests.MessageRequest;
import com.e_messenger.code.dto.responses.ApiResponse;
import com.e_messenger.code.dto.responses.MessageResponse;
import com.e_messenger.code.entity.Message;
import com.e_messenger.code.mapstruct.MessageMapper;
import com.e_messenger.code.service.impl.ChattingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

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

    @MessageMapping("/send-message")
    public void sendMessage(SendMessageRequest request) {
        mainService.sendMessage(request.conversationId, request.messageRequest);
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

    record SendMessageRequest(String conversationId, MessageRequest messageRequest){}
}
