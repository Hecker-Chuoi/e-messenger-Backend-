package com.e_messenger.code.service;

import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.Message;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.mapstruct.ConversationMapper;
import com.e_messenger.code.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
    ConversationRepository mainRepo;
    UserService userService;
    ConversationMapper conversationMapper;

    private String getDirectChatId(User curUser, User other){
        String id1 = curUser.getId();
        String id2 = other.getId();

        if(id1.compareTo(id2) > 0){
            String tmp = id1;
            id1 = id2;
            id2 = tmp;
        }
        return id1 + "-" + id2;
    }

    public Conversation createDirectChat(String otherIdentifier){
        User curUser = userService.getMyInfo();
        User other = userService.getUserByIdentifier(otherIdentifier);

        if(curUser.equals(other))
            throw new AppException(StatusCode.UNCATEGORIZED);

        Conversation newDirect = Conversation.builder()
                .id(getDirectChatId(curUser, other))
                .type(Conversation.ConversationType.DIRECT)
                .participantIds(List.of(curUser.getId(), other.getId()))
                .build();
        return mainRepo.save(newDirect);
    }

    public Conversation getDirectChat(String otherIdentifier){
        User curUser = userService.getMyInfo();
        User other = userService.getUserByIdentifier(otherIdentifier);

        if(curUser.equals(other))
            throw new AppException(StatusCode.UNCATEGORIZED);

        return mainRepo.findConversationById(getDirectChatId(curUser, other)).orElseThrow(
                () -> new AppException(StatusCode.UNCATEGORIZED)
        );
    }

    private String getDirectChatPattern(String id){
        return "^%s|%s$".formatted(id, id);
    }

    public List<Conversation> getAllDirectChat(){
        User curUser = userService.getMyInfo();

        List<Conversation> result = mainRepo.getAllDirectChat(getDirectChatPattern(curUser.getId()));
        for(Conversation conv : result){
            List<String> ids = conv.getParticipantIds();
            String otherId = ids.getFirst().equals(curUser.getId()) ? ids.getLast() : ids.getFirst();
            conv.setConversationName(userService.getUserById(otherId).getDisplayName());
        }

        return result;
    }

    public void updateLastSentInfo(String groupsId, Message message){
        Conversation conversation = mainRepo.findConversationById(groupsId).orElseThrow(
                () -> new AppException(StatusCode.UNCATEGORIZED)
        );

        if(conversation.getLastMessageTime() != null && message.getSentAt().isBefore(conversation.getLastMessageTime()))
            throw new AppException(StatusCode.UNCATEGORIZED);

        conversationMapper.updateLastSentInfo(conversation, message);
        mainRepo.save(conversation);
    }
}
