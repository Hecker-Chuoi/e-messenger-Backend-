package com.e_messenger.code.service.impl;

import com.cloudinary.Cloudinary;
import com.e_messenger.code.dto.requests.MessageRequest;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.message.Message;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.entity.enums.GeneralType;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.mapstruct.ConversationMapper;
import com.e_messenger.code.mapstruct.MessageMapper;
import com.e_messenger.code.repository.ConversationRepository;
import com.e_messenger.code.repository.MessageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChattingService {
    MessageRepository mainRepo;
    ConversationRepository conversationRepo;

    MessageMapper messageMapper;
    ConversationMapper conversationMapper;

    UserService userService;
    Cloudinary cloudinary;
    ConversationQueryServiceImpl conversationQueryService;

    public Message sendText(Conversation conv, MessageRequest request, Principal principal){
        User curUser = userService.getUserById(principal.getName());

        Message message = Message.builder()
                .senderId(curUser.getId())
                .senderName(curUser.getDisplayName())
                .conversationId(conv.getId())
                .sentAt(LocalDateTime.now())
                .build();

        messageMapper.update(message, request);
        conversationMapper.updateLastSentInfo(conv, message);

        conversationRepo.save(conv);
        return mainRepo.save(message);
    }

    public Message sendFile(Conversation conv, MessageRequest request, Principal principal) throws IOException {
        String[] encoded = request.getContent().split(";");
        String mimeType = encoded[0].split(":")[1];
        String base64 = encoded[1].split(",")[1];

        String generalType = mimeType.split("/")[0];
        String specificType = mimeType.split("/")[1];
        System.out.println(generalType + " " + specificType);

        if(!generalType.toUpperCase().equals(request.getType().toString()))
            throw new AppException(StatusCode.UNCATEGORIZED);

        User curUser = userService.getUserById(principal.getName());

        byte[] fileBytes = Base64.getDecoder().decode(base64);
        Map config = new HashMap();
        config.put("resource_type", GeneralType.fromString(generalType).getUploadOption());
        config.put("folder", "Conversation/%s".formatted(conv.getId()));

        Map result = cloudinary.uploader().upload(fileBytes, config);

        Message message = Message.builder()
                .content(result.get("url").toString())
                .type(request.getType())
                .senderId(curUser.getId())
                .senderName(curUser.getDisplayName())
                .conversationId(conv.getId())
                .sentAt(LocalDateTime.now())
                .build();

        conversationMapper.updateLastSentInfo(conv, message);

        conversationRepo.save(conv);
        return mainRepo.save((message));
    }

    public List<Message> getMessageHistory(String conversationId, int pageNum, int pageSize){
        Conversation conversation = conversationQueryService.getConversationById(conversationId, userService.getCurrentUser().getId());
        return mainRepo.findMessagesByConversationId(conversationId,
                PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.desc("sentAt"))))
                .stream().toList();
    }
}
