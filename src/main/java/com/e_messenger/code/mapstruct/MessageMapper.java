package com.e_messenger.code.mapstruct;

import com.e_messenger.code.dto.responses.MessageResponse;
import com.e_messenger.code.entity.message.Message;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageResponse toResponse(Message message);
    List<MessageResponse> toResponses(List<Message> messages);
}
