package com.e_messenger.code.service.impl;

import com.e_messenger.code.entity.Conversation;
import com.e_messenger.code.entity.Participant;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.entity.enums.ConversationType;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.e_messenger.code.repository.ConversationRepository;
import com.e_messenger.code.service.ConversationQueryService;
import com.e_messenger.code.utils.ParticipantUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationQueryServiceImpl implements ConversationQueryService {
    UserService userService;
    ConversationRepository conversationRepo;
    ParticipantUtil participantUtil;

    MongoTemplate mongoTemplate;

    static public String getDirectChatId(User curUser, User other) {
        String id1 = curUser.getId();
        String id2 = other.getId();

        if (id1.compareTo(id2) > 0) {
            String tmp = id1;
            id1 = id2;
            id2 = tmp;
        }
        return id1 + "-" + id2;
    }

    private void updateConversationInfo(Conversation conv, String curUserId) {
        if (!conv.getType().equals(ConversationType.DIRECT))
            return;

        User other = null;
        for (Participant participant : conv.getParticipants()) {
            if (!participant.getParticipantId().equals(curUserId)) {
                other = userService.getUserById(participant.getParticipantId());
            }
        }

        if (other == null) {
            throw new AppException(StatusCode.UNCATEGORIZED);
        }

        // set conversation's name by other's name
        conv.setConversationName(other.getDisplayName());
        // set conversation's avatar by other's avatar
        conv.setAvatarUrl(other.getAvatarUrl());
    }

    private String getDirectChatPattern(String id) {
        return "^%s-|-%s$".formatted(id, id);
    }
    // scenario: find other by email, use user's id to call this method to get conversation

    @Override
    public Conversation getDirectChat(String otherId) {
        User curUser = userService.getCurrentUser();
        User other = userService.getUserById(otherId);

        if (curUser.equals(other))
            throw new AppException(StatusCode.CONVERSATION_NOT_FOUND);

        Conversation conv = conversationRepo.findConversationById(getDirectChatId(curUser, other)).orElseThrow(
                () -> new AppException(StatusCode.CONVERSATION_NOT_FOUND)
        );
        conv.setConversationName(other.getDisplayName());
        conv.setAvatarUrl(other.getAvatarUrl());
        conv.setParticipants(getParticipants(conv));

        return conv;
    }

    @Override
    public Conversation getConversationById(String conversationId, String userId) {
        Conversation conv = conversationRepo.findConversationById(conversationId).orElseThrow(
                () -> new AppException(StatusCode.UNCATEGORIZED)
        );

        if (!participantUtil.hasConversationAccess(conv, userId))
            throw new AppException(StatusCode.UNCATEGORIZED);

        updateConversationInfo(conv, userId);
        conv.setParticipants(getParticipants(conv));

        return conv;
    }

    private List<Participant> getParticipants(Conversation group) {
        List<Participant> result = group.getParticipants();
        for (Participant x : result) {
            User user = userService.getUserById(x.getParticipantId());
            x.setAvatarUrl(user.getAvatarUrl());
            x.setDisplayName(user.getDisplayName());
            x.setFcmToken(user.getFcmToken());
        }
        return result;
    }

    @Override
    public List<Conversation> getAllDirectChat(int pageNum, int pageSize) {
        User curUser = userService.getCurrentUser();

        List<Conversation> result = conversationRepo.findAllDirectChat(getDirectChatPattern(curUser.getId()),
                PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.desc("lastMessageTime")))
        );
        for (Conversation conv : result) {
            updateConversationInfo(conv, curUser.getId());
            conv.setParticipants(getParticipants(conv));
        }

        return result;
    }

    @Override
    public List<Conversation> getAllGroupChat(int pageNum, int pageSize) {
        User curUser = userService.getCurrentUser();

        List<Conversation> result = conversationRepo.findConversationByParticipantIdsContainingAndType(
                curUser.getId(), ConversationType.GROUP,
                PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.desc("lastMessageTime")))
        );

        for (Conversation conv : result) {
            conv.setParticipants(getParticipants(conv));
        }

        return result;
    }

    @Override
    public List<Conversation> getAllConversation(int pageNum, int pageSize) {
        User curUser = userService.getCurrentUser();

        List<Conversation> result =
                conversationRepo.findConversationByParticipantIdsContaining(curUser.getId(),
                        PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.desc("lastMessageTime")))
                );

        for (Conversation conv : result) {
            updateConversationInfo(conv, curUser.getId());
            conv.setParticipants(getParticipants(conv));
        }

        return result;
    }

    public List<Conversation> searchConversation(String key, int pageNum, int pageSize, Principal principal) {
        Aggregation aggregation = Aggregation.newAggregation(
            // Step 1: Filter conversations that the user participates in
            Aggregation.match(Criteria.where("participants.participantId").is(principal.getName())),

            // Step 2: Add 'otherParticipant' for direct conversations (exclude self)
            Aggregation.addFields()
                .addField("otherParticipant").withValue(
                    ConditionalOperators.when(Criteria.where("type").is("DIRECT"))
                        .then(
                            ArrayOperators.Filter.filter("participants")
                                .as("p")
                                .by(ComparisonOperators.Ne.valueOf("p.participantId").notEqualToValue(principal.getName()))
                        )
                        .otherwise(Collections.emptyList())
                ).build(),

            // Step 3: Unwind otherParticipant for direct
            Aggregation.unwind("otherParticipant", true),

            // Step 4: Lookup user info for otherParticipant
            Aggregation.lookup("users", "otherParticipant.participantId", "_id", "otherUser"),

            Aggregation.unwind("otherUser", true),

            // Step 5: Set conversationName for direct = otherUser.displayName
            Aggregation.addFields()
                .addFieldWithValue("conversationName",
                    ConditionalOperators.when(Criteria.where("type").is("DIRECT"))
                        .thenValueOf("otherUser.displayName")
                        .otherwiseValueOf("conversationName")
                ).build(),

            Aggregation.addFields()
                .addFieldWithValue("avatarUrl",
                    ConditionalOperators.when(Criteria.where("type").is("DIRECT"))
                        .thenValueOf("otherUser.avatarUrl")
                        .otherwiseValueOf("avatarUrl")
                ).build(),

            // Step 6: Match conversationName with key (i = case-insensitive)
            Aggregation.match(Criteria.where("conversationName").regex(".*" + Pattern.quote(key) + ".*")),

            Aggregation.sort(Sort.by(Sort.Order.desc("lastMessageTime"))),

            Aggregation.skip((long) pageNum * pageSize),

            Aggregation.limit(pageSize)
        );

        return mongoTemplate.aggregate(aggregation, "conversations", Conversation.class).getMappedResults();
    }
}
