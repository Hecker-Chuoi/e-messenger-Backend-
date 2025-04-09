package com.e_messenger.code.controller;

import com.e_messenger.code.dto.requests.MessageRequest;
import com.e_messenger.code.dto.responses.ApiResponse;
import com.e_messenger.code.dto.responses.MessageResponse;
import com.e_messenger.code.entity.Message;
import com.e_messenger.code.mapstruct.MessageMapper;
import com.e_messenger.code.service.DirectMessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/direct")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "user token")
public class DirectMessageController {
    DirectMessageService service;
    MessageMapper mapper;

    @PostMapping("/{otherId}")
    public ApiResponse<MessageResponse> sendMessage(@PathVariable String otherId, @RequestBody MessageRequest request){
        Message result = service.sendMessageToUser(otherId, request);
        return ApiResponse.<MessageResponse>builder()
                .result(mapper.toResponse(result))
                .build();
    }

    @GetMapping("/{otherId}")
    public ApiResponse<List<MessageResponse>> getDirectMessage(@PathVariable String otherId){
        List<Message> result = service.getDirectMessages(otherId);
        return ApiResponse.<List<MessageResponse>>builder()
                .result(mapper.toResponses(result))
                .build();
    }
}
