package com.e_messenger.code.service.impl;

import com.e_messenger.code.dto.requests.message.MediaMessageRequest;
import com.e_messenger.code.dto.requests.message.TextMessageRequest;
import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.enums.GeneralType;
import com.e_messenger.code.entity.enums.MediaType;
import com.e_messenger.code.entity.message.MediaMessage;
import com.e_messenger.code.entity.message.Message;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.entity.message.TextMessage;
import com.e_messenger.code.mapstruct.ConversationMapper;
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
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChattingService {
    MessageRepository mainRepo;
    ConversationRepository conversationRepo;

    ConversationMapper conversationMapper;

    UserService userService;
    ConversationQueryServiceImpl conversationQueryService;
    private final NotificationService notificationService;

    public Message sendText(String conversationId, TextMessageRequest request, Principal principal){
        User curUser = userService.getUserById(principal.getName());
        Conversation conv = conversationQueryService.getConversationById(conversationId, principal.getName());

        TextMessage message = TextMessage.builder()
                .content(request.getText())
                .actorId(curUser.getId())
                .actorName(curUser.getDisplayName())
                .actorAvatarUrl(curUser.getAvatarUrl())
                .conversationId(conversationId)
                .time(Instant.now())
                .type(GeneralType.TEXT)
                .build();

        mainRepo.save(message);
        conversationMapper.updateLastSentInfo(conv, message); // for display purpose
        conversationRepo.save(conv);

        notificationService.notifyNewMessage(conv, message);
        return message;
    }

    public Message sendFile(String conversationId, MediaMessageRequest request, Principal principal) throws IOException {
        User curUser = userService.getUserById(principal.getName());
        Conversation conv = conversationQueryService.getConversationById(conversationId, principal.getName());

        MediaMessage message = MediaMessage.builder()
                .content(request.getMediaType().equals(MediaType.IMAGE) ? "Image" : "Audio")
                .mediaType(request.getMediaType())
                .url(request.getUploadedUrl())
                .actorId(curUser.getId())
                .actorName(curUser.getDisplayName())
                .actorAvatarUrl(curUser.getAvatarUrl())
                .conversationId(conv.getId())
                .time(Instant.now())
                .type(GeneralType.MEDIA)
                .build();

        conversationMapper.updateLastSentInfo(conv, message);

        conversationRepo.save(conv);
        message = mainRepo.save(message);

        notificationService.notifyNewMessage(conv, message);

        return message;
    }

    public List<Message> getMessageHistory(String conversationId, int pageNum, int pageSize){
        Conversation conversation = conversationQueryService.getConversationById(conversationId, userService.getCurrentUser().getId());
        return mainRepo.findMessagesByConversationId(conversationId,
                PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.desc("sentAt"))))
                .stream().toList();
    }
}
