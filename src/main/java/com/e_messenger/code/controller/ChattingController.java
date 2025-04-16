package com.e_messenger.code.controller;

import com.e_messenger.code.dto.requests.MessageRequest;
import com.e_messenger.code.dto.responses.ApiResponse;
import com.e_messenger.code.dto.responses.MessageResponse;
import com.e_messenger.code.entity.Message;
import com.e_messenger.code.mapstruct.MessageMapper;
import com.e_messenger.code.service.impl.ChattingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatting")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "user token")
public class ChattingController {
    ChattingService mainService;
    MessageMapper messageMapper;

    @PostMapping("/{conversationId}/send")
    public ApiResponse<MessageResponse> sendMessage(@PathVariable String conversationId, @RequestBody MessageRequest request){
        Message result = mainService.sendMessage(conversationId, request);
        return ApiResponse.<MessageResponse>builder()
                .result(messageMapper.toResponse(result))
                .build();
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
